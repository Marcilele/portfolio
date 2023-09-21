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
 * Calculate the number of products in different subcategories
 */
object SubCategoryCountApp {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setAppName("SubCategoryCountApp")
      .setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(5))

    ssc.checkpoint("cp")

    val topic = "flipkartproductsReplication2"
    val groupId = "SubCategoryCountApp"

    val recordDStream: InputDStream[ConsumerRecord[String,String]] = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)

    val subCategoryCountMapDStream: DStream[(String,Int)] = recordDStream.map({
      record => {
        val jsonObject: JSONObject = JSON.parseObject(record.value())
        val subCategory: String = Option(jsonObject.getString("sub_category")).getOrElse("")
        (subCategory,1)
      }
    })

    val subCategoryCountDStream: DStream[(String,Int)] = subCategoryCountMapDStream.updateStateByKey(
      (seq: Seq[Int], buff: Option[Int]) => {
        val newCount = buff.getOrElse(0) + seq.sum
        Option(newCount)
      }
    )

    subCategoryCountDStream.print(100)

//    //Output the result to MySQL
//    subCategoryCountDStream.foreachRDD(rdd => {
//
//      val filteredRDD = rdd.filter { case (subCategory, _) => subCategory != "" }
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

    //Output the result to Redis

    subCategoryCountDStream.foreachRDD(rdd => {
      val filteredRDD = rdd.filter { case (subCategory, _) => subCategory != "" }

      def func(records: Iterator[(String, Int)]) {
        var jedis: Jedis = null
        try {
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
