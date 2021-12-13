package com.sun.task;

import com.sun.agg.JobResourceCountAgg;
import com.sun.bean.AvgResourceAccumulatorEntity;
import com.sun.bean.AvgResourceEntity;
import com.sun.bean.JobResourceInfoEntity;
import com.sun.bean.KVEntity;
import com.sun.config.JedisPoolConfig;
import com.sun.map.JobResourceInfoMapFunction;
import com.sun.mapper.KVEntityRedisMapper;
import com.sun.process.CountResourceProcessFunction;
import com.sun.util.PropertiesUtil;
import com.sun.window.ResourceProcessWindowFunction;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.net.URL;
import java.util.Properties;

public class UserResourceCountTask {
    static OutputTag<KVEntity> memTag = new OutputTag<KVEntity>("mem") {
    };
    static OutputTag<KVEntity> gpuTag = new OutputTag<KVEntity>("gpu") {
    };

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);
        Properties properties = PropertiesUtil.getKafkaProperties("slurm-job-info");
        DataStreamSource<String> inputStream = env.addSource(new FlinkKafkaConsumer<String>("user-job-resource", new SimpleStringSchema(), properties));
//        URL resource = ExperimentCountTask.class.getResource("/JobInfo.csv");
//        DataStream<String> inputStream = env.readTextFile(resource.getPath());


        DataStream<JobResourceInfoEntity> dataStream = inputStream.map(new JobResourceInfoMapFunction()).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<JobResourceInfoEntity>(Time.seconds(10)) {
            @Override
            public long extractTimestamp(JobResourceInfoEntity element) {
                return element.getTimestamp() * 1000L;
            }
        });

        //按照userId分组开窗统计
        DataStream<AvgResourceEntity> aggregateStream = dataStream
                .keyBy(JobResourceInfoEntity::getUserId)
                .timeWindow(Time.minutes(10), Time.seconds(5))
                .allowedLateness(Time.seconds(5))
                .aggregate(new JobResourceCountAgg(), new ResourceProcessWindowFunction());

        SingleOutputStreamOperator<KVEntity> resultStream = aggregateStream.keyBy(AvgResourceEntity::getUserId)
                .process(new CountResourceProcessFunction());
        DataStream<KVEntity> memStream = resultStream.getSideOutput(memTag);
        DataStream<KVEntity> gpuStream = resultStream.getSideOutput(gpuTag);
        resultStream.addSink(new RedisSink<>(new JedisPoolConfig().getJedisPoolConfig(), new KVEntityRedisMapper()));
        memStream.addSink(new RedisSink<>(new JedisPoolConfig().getJedisPoolConfig(), new KVEntityRedisMapper()));
        gpuStream.addSink(new RedisSink<>(new JedisPoolConfig().getJedisPoolConfig(), new KVEntityRedisMapper()));
        env.execute();
    }


}
