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
 * 统计原始价格与折扣的关系
 */
object ActualPriceAndDiscountApp {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setAppName("ActualPriceAndDiscountApp")
      .setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(5))

    //使用有状态操作时，需要设定检查点路径
    ssc.checkpoint("cp")

    //kafka主题
    val topic = "flipkartfashionproducts1"
    //消费者组
    val groupId = "ActualPriceAndDiscountApp"

    //消费kafka数据
    val recordDStream: InputDStream[ConsumerRecord[String,String]] = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)

    //提取原始价格和销售价格，并计算折扣率
    val actualPriceAndDiscountMapDStream: DStream[(String,Double)] = recordDStream.map({
      record => {
        //将json格式字符串转换为json对象
        val jsonObject: JSONObject = JSON.parseObject(record.value())
        //从json对象中获取原始价格
        val actualPrice: Int = jsonObject.getInteger("actual_price")
        //从json对象中获取销售价格
        val sellingprice: Int = jsonObject.getInteger("selling_price")
        //计算折扣率
        var discount: Double = 0.0
        if (actualPrice != 0) {
          val discountDecimal = BigDecimal(actualPrice - sellingprice)
            ./(BigDecimal(actualPrice))
          discount = discountDecimal.setScale(4, BigDecimal.RoundingMode.HALF_UP).toDouble
        } else {
          discount = 0.0 // 如果actualPrice为零，则折扣率设为0
        }
        (actualPrice.toString,discount)
      }
    })
    actualPriceAndDiscountMapDStream.print(100);

    //根据Key对数据的状态进行更新，求每个价格的最大折扣率
    val maxDiscountByActualPriceDStream: DStream[(String, Double)] = actualPriceAndDiscountMapDStream.updateStateByKey(
      (newDiscounts: Seq[Double], currentState: Option[Double]) => {
        val maxDiscount = if (newDiscounts.nonEmpty) newDiscounts.max else 0.0 // 添加非空判断
        val previousMaxDiscount = currentState.getOrElse(0.0) // 获取之前的最大折扣（如果有）
        Some(math.max(maxDiscount, previousMaxDiscount)) // 返回更新后的最大折扣
      }
    )

    maxDiscountByActualPriceDStream.print(100)

    //把结果输出到MySQL中
    maxDiscountByActualPriceDStream.foreachRDD(rdd => {
      def func(records: Iterator[(String,Double)]) {
        var conn: Connection = null
        var stmt: PreparedStatement = null
        try {
          //定义MySQL是链接方式及其用户名和密码
          val url = "jdbc:mysql://localhost:3306/movieandecdb?useUnicode=true&characterEncoding=UTF-8"
          val user = "root"
          val password = "999999999"
          conn = DriverManager.getConnection(url, user, password)
          records.foreach(p => {
            val sql = "insert into actualpriceanddiscount(actual_price,discount) values (?,?) on duplicate key update discount=?"
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, p._1.toInt)
            stmt.setDouble(2,p._2.toDouble)
            stmt.setDouble(3,p._2.toDouble)
            stmt.executeUpdate()
          })
        } catch {
          case e: Exception => e.printStackTrace()
        } finally {
          if (stmt != null) {
            stmt.close()
          }
          if (conn != null) {
            conn.close()
          }
        }
      }

      val repartitionedRDD = rdd.repartition(3)
      repartitionedRDD.foreachPartition(func)
    })

    //把结果输出到Redis中
    maxDiscountByActualPriceDStream.foreachRDD(rdd => {
      def func(records: Iterator[(String,Double)]) {
        var jedis: Jedis = null
        try {
          //获取redis的连接
          jedis = MyRedisUtil.getJedisClient()
          records.foreach(p => {
            jedis.hset("actualpriceanddiscount",p._1,p._2.toString)
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
