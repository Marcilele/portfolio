package com.bigdata.utils

import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, streaming}
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

//Read Kafka's util class


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

  //Kafka consumer configuration
  var kafkaParams = collection.mutable.Map(
  //connect to Kafka cluster
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> broker_list,
  //Key and Value deserialization
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
  //set consumer group ID —— group.id
    ConsumerConfig.GROUP_ID_CONFIG -> "ec_group",
  //set auto reset offset to earliest or latest
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest",

    //set auto commit
  //If it is true, the offset of this consumer will be automatically submitted in the background, but Kafka is prone to data loss when it crashes
  //If it is false, we will need to manually maintain the offset of Kafka
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (false: java.lang.Boolean)
  )

  //create DStream, return the received input parameters
  def getKafkaStream(topic: String, ssc: StreamingContext): InputDStream[ConsumerRecord[String,String]] = {
    val DStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(topic), kafkaParams)
    )
    DStream
  }

  //When consuming Kafka data, specify the consumer group
  def getKafkaStream(topic: String, ssc: StreamingContext, groupId: String): InputDStream[ConsumerRecord[String,String]] = {
    kafkaParams("group.id") = groupId
    val DStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(topic), kafkaParams)
    )
    DStream
  }


  //Consume data from the specified offset position
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
