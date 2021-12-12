package com.sun.process;

import com.sun.bean.UserJobInfoEntity;
import com.sun.client.RedisClient;
import com.sun.util.DateFormatTrans;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import redis.clients.jedis.Jedis;

public class JobCountProcessFunction extends ProcessFunction<UserJobInfoEntity, String> {
    Jedis jedis;

    @Override
    public void open(Configuration parameters) throws Exception {
        jedis = RedisClient.getJedis();
    }

    @Override
    public void processElement(UserJobInfoEntity userJobInfoEntity, ProcessFunction<UserJobInfoEntity, String>.Context ctx, Collector<String> out) throws Exception {
        String jobId = String.valueOf(userJobInfoEntity.getJobId());
        String jobKey = "job_" + jobId;
        String userId = userJobInfoEntity.getUserId();
        String userKey = "user_" + userId;
        Boolean isExists = jedis.exists(jobKey);
        String timestamp = String.valueOf(userJobInfoEntity.getTimestamp());
        String dataStr = DateFormatTrans.timestampToString(timestamp + "000").substring(0, 7);
        String userDateCountKey = userKey + "_" + dataStr;
        String jobStatus = userJobInfoEntity.getJobStatus();
        if (isExists) {
            //如果redis存在当前job,则判断是否需要更新
            long preTimestamp = Long.parseLong(jedis.hget(jobKey, "timestamp"));
            if (Long.parseLong(timestamp) > preTimestamp) {
                //新数据到来，需要更新状态
                String preStatus = jedis.hget(jobKey, "status");
                jedis.hset(jobKey, "status", jobStatus);
                jedis.hset(jobKey, "timestamp", timestamp);

                //更新用户job count
                jedis.hincrBy(userKey, preStatus, -1L);
                jedis.hincrBy(userKey, jobStatus, 1L);
                //更新总job count
                jedis.hincrBy("totalJob", preStatus, -1L);
                jedis.hincrBy("totalJob", jobStatus, 1L);
                //更新userDateCount
                jedis.hincrBy(userDateCountKey, preStatus, -1L);
                jedis.hincrBy(userDateCountKey, jobStatus, 1L);
            }
        } else {
            //redis中不存在当前job
            jedis.incr("totalJobCount");
            jedis.hset(jobKey, "timestamp", timestamp);
            jedis.hset(jobKey, "status", jobStatus);
            jedis.expire(jobKey, 3 * 24 * 60 * 60);
            jedis.hincrBy(userKey, jobStatus, 1L);
            jedis.hincrBy("totalJob", jobStatus, 1L);
            jedis.hincrBy(userDateCountKey, jobStatus, 1L);
        }

    }

}
