package com.sun.window;

import com.sun.bean.AvgResourceAccumulatorEntity;
import com.sun.bean.AvgResourceEntity;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class ResourceProcessWindowFunction implements WindowFunction<AvgResourceAccumulatorEntity, AvgResourceEntity, String, TimeWindow> {

    @Override
    public void apply(String userId, TimeWindow window, Iterable<AvgResourceAccumulatorEntity> input, Collector<AvgResourceEntity> out) throws Exception {
        AvgResourceAccumulatorEntity entity = input.iterator().next();
        out.collect(new AvgResourceEntity(userId, entity.getCpu(), entity.getMem(), entity.getGpu(), entity.getCount(), window.getEnd()));
    }
}
