package com.bigdata.utils

import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, streaming}
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

//读取Kafka的util类
object MyKafkaUtil {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
        .setAppName("MyKafkaUtil")
        .setMaster("local[4]")

    val ssc: StreamingContext = new StreamingContext(conf,streaming.Seconds(3))

    KafkaUtils.createDirectStream(
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String,String](Array(""),
        kafkaParams)
    )

  }

  private val properties: Properties = MyPropertiesUtil.load("config.properties")
  val broker_list = properties.getProperty("kafka.broker.list")

  //Kafka消费者配置
  var kafkaParams = collection.mutable.Map(
    //连接Kafka集群
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> broker_list,
    //Key和Value的反序列化
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    //设置消费者组ID——group.id
    ConsumerConfig.GROUP_ID_CONFIG -> "ec_group",
    //设置自动重置偏移量 earliest latest
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest",
    //设置自动提交
    //如果是true，则这个消费者的偏移量会在后台自动提交，但是Kafka宕机容易丢失数据
    //如果是false，会需要手动维护Kafka的偏移量
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (false: java.lang.Boolean)
  )

  //创建DStream，返回接收到的输入参数
  def getKafkaStream(topic: String, ssc: StreamingContext): InputDStream[ConsumerRecord[String,String]] = {
    val DStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(topic), kafkaParams)
    )
    DStream
  }

  //在对Kafka数据进行消费的时候，指定消费者组
  def getKafkaStream(topic: String, ssc: StreamingContext, groupId: String): InputDStream[ConsumerRecord[String,String]] = {
    kafkaParams("group.id") = groupId
    val DStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(topic), kafkaParams)
    )
    DStream
  }

  //从指定的偏移量位置消费数据
  def getKafkaStream(topic: String, ssc: StreamingContext, offsets: Map[TopicPartition,Long], groupId: String): InputDStream[ConsumerRecord[String,String]] = {
    kafkaParams("group.id") = groupId
    val DStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(topic), kafkaParams, offsets)
    )
    DStream
  }

}
