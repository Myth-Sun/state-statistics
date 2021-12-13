package com.sun.config;

import com.sun.util.PropertiesUtil;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;

public class JedisPoolConfig {
    private static FlinkJedisPoolConfig jedisPoolConfig;

    static {
        jedisPoolConfig = new FlinkJedisPoolConfig.Builder()
                .setHost(PropertiesUtil.getProperty("redis.host"))
                .setPort(Integer.parseInt(PropertiesUtil.getProperty("redis.port")))
                .setPassword(PropertiesUtil.getProperty("redis.password"))
                .build();
    }

    public FlinkJedisPoolConfig getJedisPoolConfig(){
        return jedisPoolConfig;
    }
}
