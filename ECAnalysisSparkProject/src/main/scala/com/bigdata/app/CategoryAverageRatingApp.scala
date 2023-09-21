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
 * calculate the average rating in different categories
 */
object CategoryAverageRatingApp {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setAppName("CategoryAverageRatingApp")
      .setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(5))

    ssc.checkpoint("cp")

    val topic = "flipkartproductsReplication2"
    val groupId = "CategoryAverageRatingApp"

    val recordDStream: InputDStream[ConsumerRecord[String,String]] = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)

    //extract the category and user rating
    val categoryAverageRatingMapDStream: DStream[(String,Double)] = recordDStream.map({
      record => {
        //Convert the json format string to a json object
        val jsonObject: JSONObject = JSON.parseObject(record.value())
        //get the category from the json object
        val category: String = jsonObject.getString("category")
        //get the user rating from the json object
        val averageRating: Double = jsonObject.getDouble("average_rating")
        (category,averageRating)
      }
    })

    //Update the status of the data according to the key, first accumulate the score, and then calculate the average score
    val categoryAverageRatingSumCountDStream: DStream[(String, (Double, Int))] = categoryAverageRatingMapDStream.updateStateByKey(
      (seq: Seq[Double], buff: Option[(Double, Int)]) => {
        // Get the accumulated sum and count from the buffer or initialize to (0.0, 0)
        // Calculate the new sum by adding the sum of current sequence of ratings to previous sum
        val newSum = buff.getOrElse((0.0, 0))._1 + seq.sum
        // Calculate the new count by adding the number of current ratings to the previous count
        val newCount = buff.getOrElse((0.0, 0))._2 + seq.length
        Option((newSum, newCount))
      }
    )

    // Calculate the average rating of each category and keep one decimal place
    val categoryAverageRatingDStream: DStream[(String, Double)] = categoryAverageRatingSumCountDStream.mapValues {
      case (sum, count) => BigDecimal(sum / count.toDouble).setScale(1, BigDecimal.RoundingMode.HALF_UP).toDouble
    }

    categoryAverageRatingDStream.print(100)

//    Output the result to MySQL
//    categoryAverageRatingDStream.foreachRDD(rdd => {
//      def func(records: Iterator[(String,Double)]) {
//        var conn: Connection = null
//        var stmt: PreparedStatement = null
//        try {
//
//          val url = "jdbc:mysql://node03:3306/movieandecdb?useUnicode=true&characterEncoding=UTF-8"
//          val user = "root"
//          val password = "123456"
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

    //Output the result to Redis
    categoryAverageRatingDStream.foreachRDD(rdd => {
      def func(records: Iterator[(String,Double)]) {
        var jedis: Jedis = null
        try {

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
