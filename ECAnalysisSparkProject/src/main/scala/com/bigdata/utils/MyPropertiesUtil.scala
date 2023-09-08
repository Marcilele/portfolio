package com.bigdata.utils

import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Properties

//读取配置文件的util类
object MyPropertiesUtil {

  def main(args: Array[String]): Unit = {

    val properties: Properties = MyPropertiesUtil.load("config.properties")
    println(properties.getProperty("kafka.broker.list"))

  }

  def load(propertiesName: String): Properties = {
    val properties: Properties = new Properties()
    //加载指定的配置文件
    properties.load(new InputStreamReader(
      Thread.currentThread().getContextClassLoader.getResourceAsStream(propertiesName),
      StandardCharsets.UTF_8)
    )
    properties
  }

}
