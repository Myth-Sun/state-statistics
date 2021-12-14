package com.sun.client;


import com.sun.util.PropertiesUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class MyRedissonClient {
    public static RedissonClient getRedissonClient(){
        Config config = new Config();
        String host = PropertiesUtil.getProperty("redis.host");
        String port = PropertiesUtil.getProperty("redis.port");
        String password = PropertiesUtil.getProperty("redis.password");
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        return Redisson.create(config);
    }
}
