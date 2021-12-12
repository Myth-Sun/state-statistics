package com.sun.task;

import com.sun.bean.UserJobInfoEntity;
import com.sun.client.RedisClient;
import com.sun.map.UserJobInfoMapFunction;
import com.sun.process.JobCountProcessFunction;
import com.sun.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.util.Collector;
import redis.clients.jedis.Jedis;

import java.net.URL;
import java.util.Properties;

public class ExperimentCountTask {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        Properties properties = PropertiesUtil.getKafkaProperties("slurm-job-info");
        DataStreamSource<String> inputStream = env.addSource(new FlinkKafkaConsumer<String>("user-job", new SimpleStringSchema(), properties));
//        URL resource = ExperimentCountTask.class.getResource("/UserJobInfo.csv");
//        DataStream<String> inputStream = env.readTextFile(resource.getPath());

        DataStream<UserJobInfoEntity> dataStream = inputStream.map(new UserJobInfoMapFunction())
                .assignTimestampsAndWatermarks(new AscendingTimestampExtractor<UserJobInfoEntity>() {
                    @Override
                    public long extractAscendingTimestamp(UserJobInfoEntity element) {
                        return element.getTimestamp() * 1000L;
                    }
                });

        DataStream<String> resultDataStream = dataStream.process(new JobCountProcessFunction())
                .setParallelism(1);//设置并行度为1，避免并发问题
        env.execute();
    }

}
