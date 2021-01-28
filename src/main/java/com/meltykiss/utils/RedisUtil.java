package com.meltykiss.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis连接池
 */
public class RedisUtil {
    private static JedisPool jedisPool = null;
    public static Jedis getJedis(){
        if(jedisPool == null){
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(100);
            jedisPoolConfig.setMinIdle(4);
            jedisPoolConfig.setMaxIdle(4);
            //资源耗尽时等待
            jedisPoolConfig.setBlockWhenExhausted(true);
            jedisPoolConfig.setMaxWaitMillis(5000);
            /**
             * 从池中去连接后要进行测试，因为连接池中的连接可能坏掉（还在，但不好用了）：
             * 1 服务器端重启过
             * 2 网断过
             * 3 服务器端维持空闲连接超时
             *
             * 如果不设置，经常因为外界因素造成莫名其妙的数据丢失问题，很奇葩。你只能管理自己，没法管理别人
             */
            jedisPoolConfig.setTestOnBorrow(true);
            /**
             * 空闲时偶尔测一下
             */
            jedisPoolConfig.setTestWhileIdle(true);
            jedisPool = new JedisPool(jedisPoolConfig, "hadoop102", 6379);
        }
        return jedisPool.getResource();
    }

    /**
     * 使用
     * Jedis jedis = RedisUtil.getJedis();
     * String pong = jedis.ping();
     * jedis.close();
     *
     * 所以为什么这里用close关闭呢？Jedis重写了close()，要是你是new的，它就直接关闭；要是从连接池得到的，它会返回给连接池
     */
}
