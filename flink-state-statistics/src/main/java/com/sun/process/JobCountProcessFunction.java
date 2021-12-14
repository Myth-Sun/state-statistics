package com.sun.process;

import com.sun.bean.UserJobInfoEntity;
import com.sun.client.MyRedissonClient;
import com.sun.client.RedisClient;
import com.sun.util.DateFormatTrans;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.redisson.RedissonKeys;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

public class JobCountProcessFunction extends ProcessFunction<UserJobInfoEntity, String> {
    Jedis jedis;
    RedissonClient redissonClient;

    @Override
    public void open(Configuration parameters) throws Exception {
        jedis = RedisClient.getJedis();
        redissonClient = MyRedissonClient.getRedissonClient();
    }

    @Override
    public void processElement(UserJobInfoEntity userJobInfoEntity, ProcessFunction<UserJobInfoEntity, String>.Context ctx, Collector<String> out) throws Exception {
        String jobId = String.valueOf(userJobInfoEntity.getJobId());
        String jobKey = "job_" + jobId;
        String userId = userJobInfoEntity.getUserId();
        String userKey = "user_" + userId;
        String timestamp = String.valueOf(userJobInfoEntity.getTimestamp());
        String dataStr = DateFormatTrans.timestampToString(timestamp + "000").substring(0, 7);
        String userDateCountKey = userKey + "_" + dataStr;
        String jobStatus = userJobInfoEntity.getJobStatus();
        RLock userJobLock = redissonClient.getLock("userJobLock");
        try {
            boolean isLock = userJobLock.tryLock(100, 10, TimeUnit.SECONDS);
            if (isLock) {
                RMap<String, String> jobMap = redissonClient.getMap(jobKey, StringCodec.INSTANCE);
                RMap<String, Long> userMap = redissonClient.getMap(userKey, StringCodec.INSTANCE);
                RMap<String, Long> totalJobMap = redissonClient.getMap("totalJob", StringCodec.INSTANCE);
                RMap<String, Long> userDataCountMap = redissonClient.getMap(userDateCountKey, StringCodec.INSTANCE);
                boolean isExists = jobMap.isExists();

                if (isExists) {
                    long preTimestamp = Long.parseLong(jobMap.get("timestamp"));
                    if (Long.parseLong(timestamp) > preTimestamp) {
                        //新数据到来，需要更新状态
                        String preStatus = jobMap.get("status");
                        jobMap.fastPut("status", jobStatus);
                        jobMap.fastPut("timestamp", timestamp);

                        //更新用户job count
                        userMap.addAndGet(preStatus, -1L);
                        userMap.addAndGet(jobStatus, 1L);
                        //更新总job count
                        totalJobMap.addAndGet(preStatus, -1L);
                        totalJobMap.addAndGet(jobStatus, 1L);
                        //更新userDateCount
                        userDataCountMap.addAndGet(preStatus, -1L);
                        userDataCountMap.addAndGet(jobStatus, 1L);
                    }
                } else {
                    RAtomicLong totalJobCount = redissonClient.getAtomicLong("totalJobCount");
                    totalJobCount.addAndGet(1L);
                    jobMap.fastPutIfAbsent("timestamp", timestamp);
                    jobMap.fastPutIfAbsent("status", jobStatus);
                    jobMap.expire(3, TimeUnit.DAYS);
                    userMap.addAndGet(jobStatus, 1L);
                    totalJobMap.addAndGet(jobStatus, 1L);
                    userDataCountMap.addAndGet(jobStatus, 1L);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("锁已释放");
            userJobLock.unlock();
        }

    }

}
