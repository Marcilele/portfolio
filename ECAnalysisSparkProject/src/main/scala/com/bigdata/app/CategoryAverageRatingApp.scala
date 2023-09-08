package com.bigdata.app

import java.sql.{Connection, DriverManager, PreparedStatement}

import com.alibaba.fastjson.{JSON, JSONObject}
import com.bigdata.utils.{MyKafkaUtil, MyRedisUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import redis.clients.jedis.Jedis

/**
 * 统计不同类别的产品的平均评分
 */
object CategoryAverageRatingApp {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setAppName("CategoryAverageRatingApp")
      .setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(5))

    //使用有状态操作时，需要设定检查点路径
    ssc.checkpoint("cp")

    //kafka主题
    val topic = "flipkartfashionproducts1"
    //消费者组
    val groupId = "CategoryAverageRatingApp"

    //消费kafka数据
    val recordDStream: InputDStream[ConsumerRecord[String,String]] = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)

    //提取类别和用户评分
    val categoryAverageRatingMapDStream: DStream[(String,Double)] = recordDStream.map({
      record => {
        //将json格式字符串转换为json对象
        val jsonObject: JSONObject = JSON.parseObject(record.value())
        //从json对象中获取类别
        val category: String = jsonObject.getString("category")
        //从json对象中获取用户评分
        val averageRating: Double = jsonObject.getDouble("average_rating")
        (category,averageRating)
      }
    })

    //根据Key对数据的状态进行更新，先累加评分，之后在求评分平均值
    val categoryAverageRatingSumCountDStream: DStream[(String, (Double, Int))] = categoryAverageRatingMapDStream.updateStateByKey(
      (seq: Seq[Double], buff: Option[(Double, Int)]) => {
        val newSum = buff.getOrElse((0.0, 0))._1 + seq.sum
        val newCount = buff.getOrElse((0.0, 0))._2 + seq.length
        Option((newSum, newCount))
      }
    )

    // 计算每个类别的平均评分并保留一位小数
    val categoryAverageRatingDStream: DStream[(String, Double)] = categoryAverageRatingSumCountDStream.mapValues {
      case (sum, count) => BigDecimal(sum / count.toDouble).setScale(1, BigDecimal.RoundingMode.HALF_UP).toDouble
    }

    categoryAverageRatingDStream.print(100)

//    //把结果输出到MySQL中
//    categoryAverageRatingDStream.foreachRDD(rdd => {
//      def func(records: Iterator[(String,Double)]) {
//        var conn: Connection = null
//        var stmt: PreparedStatement = null
//        try {
//          //定义MySQL是链接方式及其用户名和密码
//          val url = "jdbc:mysql://localhost:3306/movieandecdb?useUnicode=true&characterEncoding=UTF-8"
//          val user = "root"
//          val password = "999999999"
//          conn = DriverManager.getConnection(url, user, password)
//          records.foreach(p => {
//            val sql = "insert into categoryaveragerating(category,average_rating) values (?,?) on duplicate key update average_rating=?"
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, p._1.trim)
//            stmt.setDouble(2,p._2.toDouble)
//            stmt.setDouble(3,p._2.toDouble)
//            stmt.executeUpdate()
//          })
//        } catch {
//          case e: Exception => e.printStackTrace()
//        } finally {
//          if (stmt != null) {
//            stmt.close()
//          }
//          if (conn != null) {
//            conn.close()
//          }
//        }
//      }
//
//      val repartitionedRDD = rdd.repartition(3)
//      repartitionedRDD.foreachPartition(func)
//
//    })

    //把结果输出到Redis中
    categoryAverageRatingDStream.foreachRDD(rdd => {
      def func(records: Iterator[(String,Double)]) {
        var jedis: Jedis = null
        try {
          //获取redis的连接
          jedis = MyRedisUtil.getJedisClient()
          records.foreach(p => {
            jedis.hset("categoryaveragerating",p._1,p._2.toString)
          })
        } catch {
          case e: Exception => e.printStackTrace()
        } finally {
          if (jedis != null) {
            jedis.close()
          }
        }
      }

      val repartitionedRDD = rdd.repartition(3)
      repartitionedRDD.foreachPartition(func)

    })

    ssc.start()
    ssc.awaitTermination()

  }

}
