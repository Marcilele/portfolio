package com.bigdata.utils

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

//get the Jedis client's Util class
object MyRedisUtil {

  //Define a connection pool object
  private var jedisPool: JedisPool = null

  //get the Jedis client
  def getJedisClient(): Jedis = {
    if(jedisPool == null){
      build()
    }
    jedisPool.getResource
  }

  //create JedisPool connection pool object
  def build(): Unit = {

    val properties = MyPropertiesUtil.load("config.properties")
    val host = properties.getProperty("redis.host")
    val port = properties.getProperty("redis.port")

    val jedisPoolConfig: JedisPoolConfig = new JedisPoolConfig()
    //Maximum number of connections
    jedisPoolConfig.setMaxTotal(10000)
    //Maximum number of idle connections
    jedisPoolConfig.setMaxIdle(20)
    //Minimum idle
    jedisPoolConfig.setMinIdle(20)
    //Whether to wait when busy
    jedisPoolConfig.setBlockWhenExhausted(true)
    //Duration of waiting when busy in milliseconds
    jedisPoolConfig.setMaxWaitMillis(5000)
    //Test each time you get a connection

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
