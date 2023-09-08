package com.bigdata.utils

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

//获取Jedis客户端的Util类
object MyRedisUtil {

  //定义一个连接池对象
  private var jedisPool: JedisPool = null

  //获取Jedis客户端
  def getJedisClient(): Jedis = {
    if(jedisPool == null){
      build()
    }
    jedisPool.getResource
  }

  //创建JedisPool连接池对象
  def build(): Unit = {

    val properties = MyPropertiesUtil.load("config.properties")
    val host = properties.getProperty("redis.host")
    val port = properties.getProperty("redis.port")

    val jedisPoolConfig: JedisPoolConfig = new JedisPoolConfig()
    //最大连接数
    jedisPoolConfig.setMaxTotal(10000)
    //最大空闲
    jedisPoolConfig.setMaxIdle(20)
    //最小空闲
    jedisPoolConfig.setMinIdle(20)
    //忙碌时是否等待
    jedisPoolConfig.setBlockWhenExhausted(true)
    //忙碌时等待时长 毫秒
    jedisPoolConfig.setMaxWaitMillis(5000)
    //每次获得连接的进行测试
    jedisPoolConfig.setTestOnBorrow(true)

    jedisPool = new JedisPool(
      jedisPoolConfig,
      host,
      port.toInt
    )
  }

  def main(args: Array[String]): Unit = {

    val jedis = getJedisClient()
    println(jedis.ping())

  }

}
