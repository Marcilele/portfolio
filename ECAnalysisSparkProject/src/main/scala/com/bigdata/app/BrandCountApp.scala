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
 * calculate the number of products in different brands
 *
 */
object BrandCountApp {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setAppName("BrandCountApp")
      .setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(5))

  //When using stateful operations, set the checkpoint path
    ssc.checkpoint("cp")

    val topic = "flipkartproductsReplication2"
    val groupId = "BrandCountApp"

    val recordDStream: InputDStream[ConsumerRecord[String,String]] = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)

    //extract the brand
    val brandCountMapDStream: DStream[(String,Int)] = recordDStream.map({
      record => {
        //Convert the json format string to a json object
        val jsonObject: JSONObject = JSON.parseObject(record.value())
        //get the brand from the json object
        val brand: String = Option(jsonObject.getString("brand")).getOrElse("")
        //use the brand as the key, and 1 as the value
        (brand,1)
      }
    })

    //Update the status of the data according to the key
    val brandCountDStream: DStream[(String,Int)] = brandCountMapDStream.updateStateByKey(
      (seq: Seq[Int], buff: Option[Int]) => {
        val newCount = buff.getOrElse(0) + seq.sum
        Option(newCount)
      }
    )

    brandCountDStream.print(100)

//    Output the result to MySQL
//    brandCountDStream.foreachRDD(rdd => {
//
////      Filter out data with empty strings
//      val filteredRDD = rdd.filter { case (brand, _) => brand != "" }
//
//      def func(records: Iterator[(String,Int)]) {
//        var conn: Connection = null
//        var stmt: PreparedStatement = null
//        try {
//          //define the MySQL connection method and its username and password
//          val url = "jdbc:mysql://node03:3306/movieandecdb?useUnicode=true&characterEncoding=UTF-8"
//          val user = "root"
//          val password = "123456"
//          conn = DriverManager.getConnection(url, user, password)
//          records.foreach(p => {
//            val sql = "insert into brandcount(brand,count) values (?,?) on duplicate key update count=?"
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
    brandCountDStream.foreachRDD(rdd => {
      //Filter out data with empty strings
      val filteredRDD = rdd.filter { case (brand, _) => brand != "" }
      def func(records: Iterator[(String,Int)]) {
        var jedis: Jedis = null
        try {
          //get the connection of redis
          jedis = MyRedisUtil.getJedisClient()
          records.foreach(p => {
            jedis.hset("brandcount",p._1,p._2.toString)
          })
        } catch {
          case e: Exception => e.printStackTrace()
        } finally {
          if (jedis != null) {
            jedis.close()
          }
        }
      }

      //By repartitioning to 3 partitions, it can potentially open 3 simultaneous connections to Redis,
      // allowing for parallel writes and better throughput.

      val repartitionedRDD = filteredRDD.repartition(3)
      repartitionedRDD.foreachPartition(func)

    })

    ssc.start()
    ssc.awaitTermination()

  }

}
