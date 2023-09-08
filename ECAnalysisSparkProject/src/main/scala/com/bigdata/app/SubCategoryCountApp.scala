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
 * 统计子类别的产品数量
 */
object SubCategoryCountApp {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setAppName("SubCategoryCountApp")
      .setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(5))

    //使用有状态操作时，需要设定检查点路径
    ssc.checkpoint("cp")

    //kafka主题
    val topic = "flipkartfashionproducts1"
    //消费者组
    val groupId = "SubCategoryCountApp"

    //消费kafka数据
    val recordDStream: InputDStream[ConsumerRecord[String,String]] = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)

    //提取子类别
    val subCategoryCountMapDStream: DStream[(String,Int)] = recordDStream.map({
      record => {
        //将json格式字符串转换为json对象
        val jsonObject: JSONObject = JSON.parseObject(record.value())
        //从json对象中获取子类别
        val subCategory: String = Option(jsonObject.getString("sub_category")).getOrElse("")
        //以子类别为key，1为value
        (subCategory,1)
      }
    })

    //根据Key对数据的状态进行更新
    val subCategoryCountDStream: DStream[(String,Int)] = subCategoryCountMapDStream.updateStateByKey(
      (seq: Seq[Int], buff: Option[Int]) => {
        val newCount = buff.getOrElse(0) + seq.sum
        Option(newCount)
      }
    )

    subCategoryCountDStream.print(100)

//    //把结果输出到MySQL中
//    subCategoryCountDStream.foreachRDD(rdd => {
//
//      //      过滤掉空字符串的数据
//      val filteredRDD = rdd.filter { case (subCategory, _) => subCategory != "" }
//
//      def func(records: Iterator[(String,Int)]) {
//        var conn: Connection = null
//        var stmt: PreparedStatement = null
//        try {
//          //定义MySQL是链接方式及其用户名和密码
//          val url = "jdbc:mysql://localhost:3306/movieandecdb?useUnicode=true&characterEncoding=UTF-8"
//          val user = "root"
//          val password = "999999999"
//          conn = DriverManager.getConnection(url, user, password)
//          records.foreach(p => {
//            val sql = "insert into subcategorycount(sub_category,count) values (?,?) on duplicate key update count=?"
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, p._1.trim)
//            stmt.setInt(2,p._2.toInt)
//            stmt.setInt(3,p._2.toInt)
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
//      val repartitionedRDD = filteredRDD.repartition(3)
//      repartitionedRDD.foreachPartition(func)
//    })

    //把结果输出到Redis中
    subCategoryCountDStream.foreachRDD(rdd => {
      //过滤掉空字符串的数据
      val filteredRDD = rdd.filter { case (subCategory, _) => subCategory != "" }

      def func(records: Iterator[(String, Int)]) {
        var jedis: Jedis = null
        try {
          //获取redis的连接
          jedis = MyRedisUtil.getJedisClient()
          records.foreach(p => {
            jedis.hset("subcategorycount", p._1, p._2.toString)
          })
        } catch {
          case e: Exception => e.printStackTrace()
        } finally {
          if (jedis != null) {
            jedis.close()
          }
        }
      }

      val repartitionedRDD = filteredRDD.repartition(3)
      repartitionedRDD.foreachPartition(func)

    })

    ssc.start()
    ssc.awaitTermination()

  }

}
