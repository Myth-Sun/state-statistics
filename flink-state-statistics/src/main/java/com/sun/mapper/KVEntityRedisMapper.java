package com.sun.mapper;

import com.sun.bean.KVEntity;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

public class KVEntityRedisMapper implements RedisMapper<KVEntity> {
    @Override
    public RedisCommandDescription getCommandDescription() {
        return new RedisCommandDescription(RedisCommand.HSET,"user_resource_window_count");
    }

    @Override
    public String getKeyFromData(KVEntity kvEntity) {
        return kvEntity.getKey();
    }

    @Override
    public String getValueFromData(KVEntity kvEntity) {
        return kvEntity.getValue();
    }
}
