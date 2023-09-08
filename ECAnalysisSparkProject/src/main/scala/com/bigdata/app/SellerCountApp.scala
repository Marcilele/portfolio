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
 * 统计不同卖家的产品数量
 */
object SellerCountApp {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setAppName("SellerCountApp")
      .setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(5))

    //使用有状态操作时，需要设定检查点路径
    ssc.checkpoint("cp")

    //kafka主题
    val topic = "flipkartfashionproducts1"
    //消费者组
    val groupId = "SellerCountApp"

    //消费kafka数据
    val recordDStream: InputDStream[ConsumerRecord[String,String]] = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)

    //提取卖家
    val sellerCountMapDStream: DStream[(String,Int)] = recordDStream.map({
      record => {
        //将json格式字符串转换为json对象
        val jsonObject: JSONObject = JSON.parseObject(record.value())
        //从json对象中获取卖家
        val seller: String = Option(jsonObject.getString("seller")).getOrElse("")
        //以卖家为key，1为value
        (seller,1)
      }
    })

    //根据Key对数据的状态进行更新
    val sellerCountDStream: DStream[(String,Int)] = sellerCountMapDStream.updateStateByKey(
      (seq: Seq[Int], buff: Option[Int]) => {
        val newCount = buff.getOrElse(0) + seq.sum
        Option(newCount)
      }
    )

    sellerCountDStream.print(100)

//    //把结果输出到MySQL中
//    sellerCountDStream.foreachRDD(rdd => {
//
//      //      过滤掉空字符串的数据
//      val filteredRDD = rdd.filter { case (seller, _) => seller != "" }
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
//            val sql = "insert into sellercount(seller,count) values (?,?) on duplicate key update count=?"
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
    sellerCountDStream.foreachRDD(rdd => {
      //过滤掉空字符串的数据
      val filteredRDD = rdd.filter { case (seller, _) => seller != "" }

      def func(records: Iterator[(String, Int)]) {
        var jedis: Jedis = null
        try {
          //获取redis的连接
          jedis = MyRedisUtil.getJedisClient()
          records.foreach(p => {
            jedis.hset("sellercount", p._1, p._2.toString)
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
