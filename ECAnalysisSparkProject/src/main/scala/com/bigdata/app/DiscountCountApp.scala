package com.bigdata.app


import com.bigdata.utils.{MyKafkaUtil, MyRedisUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import com.alibaba.fastjson.{JSON, JSONObject}
import redis.clients.jedis.Jedis




/**
 * calculate the total number of products with different discount
 */
object DiscountCountApp {


  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setAppName("ActualPriceAndDiscountApp")
      .setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(5))

    ssc.checkpoint("cp")

    val topic = "flipkartproductsReplication2"
    val groupId = "ActualPriceAndDiscountApp"

    val recordDStream: InputDStream[ConsumerRecord[String,String]] = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)




    //get discount, and count the number of each discount on each batch
    val dicount_map = recordDStream.map(
      {
        record => {
          val jSONObject = JSON.parseObject(record.value())
          val discountString = jSONObject.getString("discount")

          (discountString)


        }
      }
    )

    //(69% off)


// extract the number from the discount string, and assign 1 to each discount
    val numberPattern = """(\d+)""".r

    val discountFlatmap = dicount_map.flatMap(
      {
        record => {

          if(record != null){
            val extractedNumber = numberPattern.findFirstIn(record)

            extractedNumber match {
              case Some(number) => Some((number.toString, 1))
              case None => None
            }
          }else{
            None
          }


        }
      }
    )

    val discountReduce = discountFlatmap.reduceByKey(_+_)

    //update the status of the data according to the key
    val updateFunc: (Seq[Int], Option[Int]) => Option[Int] =
      (newValues: Seq[Int], state: Option[Int]) => {
      val currentCount = newValues.sum
      val previousCount = state.getOrElse(0)
      Some(currentCount + previousCount)
    }


    //updateStateByKey
    val discountUpdateStream = discountReduce.updateStateByKey(updateFunc)

    //discountUpdateStream.print(100)






//    save to mysql
//    discountUpdateStream.foreachRDD(rdd => {
//      def func(records: Iterator[(String,Int)]) {
//        var conn: Connection = null
//        var stmt: PreparedStatement = null
//        try {
//
//          val url = "jdbc:mysql://node03:3306/movieandecdb?useUnicode=true&characterEncoding=UTF-8"
//          val user = "root"
//          val password = "123456"
//          conn = DriverManager.getConnection(url, user, password)
//          records.foreach(p => {
//            val sql = "insert into discountcount(discount, count) values (?,?) on duplicate key update count=?"
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, p._1)
//            stmt.setInt(2,p._2.toInt)
//
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
//    })

    //save to redis
    discountUpdateStream.foreachRDD(rdd => {
      def func(records: Iterator[(String,Int)]) {
        var jedis: Jedis = null
        try {
          jedis = MyRedisUtil.getJedisClient()
          records.foreach(p => {
            jedis.hset("discount",p._1,p._2.toString)
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

      val repartitionedRDD = rdd.repartition(3)
      repartitionedRDD.foreachPartition(func)

    })

    ssc.start()
    ssc.awaitTermination()

  }

}

