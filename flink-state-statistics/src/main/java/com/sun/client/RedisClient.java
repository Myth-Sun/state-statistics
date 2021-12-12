package com.sun.client;

import com.sun.util.PropertiesUtil;
import redis.clients.jedis.Jedis;

public class RedisClient {

    public static Jedis getJedis() {
        Jedis jedis = new Jedis(PropertiesUtil.getProperty("redis.host"), Integer.parseInt(PropertiesUtil.getProperty("redis.port")), 10000);
        jedis.auth(PropertiesUtil.getProperty("redis.password"));
        return jedis;
    }

}
