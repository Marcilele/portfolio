package com.bigdata.visualanalysis.utils;

import com.bigdata.visualanalysis.bean.CategoryAverageRating;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Set;

@Configuration
public class RedisUtil {

    public static void main(String[] args) {

        Jedis jedis = RedisUtil.getJedis();
        Set<String> keys = jedis.hkeys("categoryaveragerating");
        for (String key : keys) {
            String value = jedis.hget("categoryaveragerating", key);
            CategoryAverageRating categoryAverageRating = new CategoryAverageRating();
            categoryAverageRating.setCategory(key);
            categoryAverageRating.setAverageRating(Double.valueOf(value));
            System.out.println(categoryAverageRating.toString());
        }

    }

    private static  JedisPool  jedisPool =null;

    public static Jedis getJedis() {

      if(jedisPool==null){
          JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
          jedisPoolConfig.setMaxTotal(200); // 最大连接数
          jedisPoolConfig.setMaxIdle(100);// 最多维持100
          jedisPoolConfig.setMinIdle(10);// 至少维持10
          jedisPoolConfig.setBlockWhenExhausted(true);
          jedisPoolConfig.setMaxWaitMillis(5000);
          jedisPoolConfig.setTestOnBorrow(true); //借走连接时测试

          jedisPool = new JedisPool(jedisPoolConfig,"node01",6379,60000);

      }
       return   jedisPool.getResource();
    }
}
