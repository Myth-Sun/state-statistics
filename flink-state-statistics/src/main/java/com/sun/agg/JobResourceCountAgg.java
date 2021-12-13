package com.sun.agg;

import com.sun.bean.AvgResourceAccumulatorEntity;
import com.sun.bean.JobResourceInfoEntity;
import org.apache.flink.api.common.functions.AggregateFunction;

public class JobResourceCountAgg implements AggregateFunction<JobResourceInfoEntity, AvgResourceAccumulatorEntity, AvgResourceAccumulatorEntity> {


    @Override
    public AvgResourceAccumulatorEntity createAccumulator() {
        return new AvgResourceAccumulatorEntity(0.0, 0.0, 0.0, 0);
    }

    @Override
    public AvgResourceAccumulatorEntity add(JobResourceInfoEntity value, AvgResourceAccumulatorEntity accumulator) {
        return new AvgResourceAccumulatorEntity(value.getCpu() + accumulator.getCpu(), value.getMem() + accumulator.getMem(), value.getGpu() + accumulator.getGpu(), accumulator.getCount() + 1);
    }

    @Override
    public AvgResourceAccumulatorEntity getResult(AvgResourceAccumulatorEntity accumulator) {
        int count = accumulator.getCount();
        return new AvgResourceAccumulatorEntity(accumulator.getCpu() / count, accumulator.getMem() / count, accumulator.getGpu() / count, count);
    }

    @Override
    public AvgResourceAccumulatorEntity merge(AvgResourceAccumulatorEntity a, AvgResourceAccumulatorEntity b) {
        return new AvgResourceAccumulatorEntity(a.getCpu() + a.getCpu(), a.getMem() + b.getMem(), a.getGpu() + b.getGpu(), a.getCount() + b.getCount());
    }
}
