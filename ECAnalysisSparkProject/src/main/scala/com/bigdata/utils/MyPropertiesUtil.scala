package com.bigdata.utils

import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Properties

//Read the util class of the configuration file
object MyPropertiesUtil {

  def main(args: Array[String]): Unit = {

    val properties: Properties = MyPropertiesUtil.load("config.properties")
    println(properties.getProperty("kafka.broker.list"))

  }

  def load(propertiesName: String): Properties = {
    val properties: Properties = new Properties()
    //load the specified configuration file
    properties.load(new InputStreamReader(
      Thread.currentThread().getContextClassLoader.getResourceAsStream(propertiesName),
      StandardCharsets.UTF_8)
    )
    properties
  }

}
