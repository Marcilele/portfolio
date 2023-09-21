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
 * Calculate the number of products with different sellers
 */
object SellerCountApp {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setAppName("SellerCountApp")
      .setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(5))

    ssc.checkpoint("cp")

    val topic = "flipkartproductsReplication2"
    val groupId = "SellerCountApp"

    val recordDStream: InputDStream[ConsumerRecord[String,String]] = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)

    //extract the seller
    val sellerCountMapDStream: DStream[(String,Int)] = recordDStream.map({
      record => {
        val jsonObject: JSONObject = JSON.parseObject(record.value())
        val seller: String = Option(jsonObject.getString("seller")).getOrElse("")
        (seller,1)
      }
    })

    // Update the status of the data according to the key
    val sellerCountDStream: DStream[(String,Int)] = sellerCountMapDStream.updateStateByKey(
      (seq: Seq[Int], buff: Option[Int]) => {
        val newCount = buff.getOrElse(0) + seq.sum
        Option(newCount)
      }
    )

    sellerCountDStream.print(100)

//    Output the result to MySQL
//    sellerCountDStream.foreachRDD(rdd => {
//
//      //Filter out data with empty strings
//      val filteredRDD = rdd.filter { case (seller, _) => seller != "" }
//
//      def func(records: Iterator[(String,Int)]) {
//        var conn: Connection = null
//        var stmt: PreparedStatement = null
//        try {
//          val url = "jdbc:mysql://node03:3306/movieandecdb?useUnicode=true&characterEncoding=UTF-8"
//          val user = "root"
//          val password = "123456"
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

    //Output the result to Redis
    sellerCountDStream.foreachRDD(rdd => {
      //Filter out data with empty strings
      val filteredRDD = rdd.filter { case (seller, _) => seller != "" }

      def func(records: Iterator[(String, Int)]) {
        var jedis: Jedis = null
        try {
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
