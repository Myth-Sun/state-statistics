package com.sun.task;

import com.sun.bean.UserJobInfoEntity;
import com.sun.map.UserJobInfoMapFunction;
import com.sun.process.JobCountProcessFunction;
import com.sun.util.PropertiesUtil;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.net.URL;
import java.util.Properties;

public class ExperimentCountTask {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
//        Properties properties = PropertiesUtil.getKafkaProperties("slurm-job-info");
//        DataStreamSource<String> inputStream = env.addSource(new FlinkKafkaConsumer<String>("user-job", new SimpleStringSchema(), properties));
        URL resource = ExperimentCountTask.class.getResource("/UserJobInfo.csv");
        DataStream<String> inputStream = env.readTextFile(resource.getPath());

        DataStream<UserJobInfoEntity> dataStream = inputStream.map(new UserJobInfoMapFunction())
                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<UserJobInfoEntity>(Time.seconds(10)) {

                    @Override
                    public long extractTimestamp(UserJobInfoEntity element) {
                        return element.getTimestamp() * 1000L;
                    }
                });

        DataStream<String> resultDataStream = dataStream.process(new JobCountProcessFunction());
//                .setParallelism(1);//设置并行度为1，避免并发问题
        env.execute();
    }

}
