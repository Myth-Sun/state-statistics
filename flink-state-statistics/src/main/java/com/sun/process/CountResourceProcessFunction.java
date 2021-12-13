package com.sun.process;

import com.sun.bean.AvgResourceEntity;
import com.sun.bean.KVEntity;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class CountResourceProcessFunction extends KeyedProcessFunction<String, AvgResourceEntity, KVEntity> {

    @Override
    public void processElement(AvgResourceEntity avgResourceEntity, KeyedProcessFunction<String, AvgResourceEntity, KVEntity>.Context ctx, Collector<KVEntity> out) throws Exception {
        out.collect(new KVEntity("user_" + avgResourceEntity.getUserId() + "_cpu", String.valueOf(avgResourceEntity.getCpu())));
        ctx.output(new OutputTag<KVEntity>("mem") {}, new KVEntity("user_" + avgResourceEntity.getUserId() + "_mem", String.valueOf(avgResourceEntity.getMem())));
        ctx.output(new OutputTag<KVEntity>("gpu") {}, new KVEntity("user_" + avgResourceEntity.getUserId() + "_gpu", String.valueOf(avgResourceEntity.getGpu())));
    }
}
