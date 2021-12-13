package com.sun.map;

import com.sun.bean.JobResourceInfoEntity;
import com.sun.bean.UserJobInfoEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

public class JobResourceInfoMapFunction implements MapFunction<String, JobResourceInfoEntity> {

    @Override
    public JobResourceInfoEntity map(String value) throws Exception {
        if (StringUtils.isNotEmpty(value)) {
            String[] fields = value.split(",");
            return new JobResourceInfoEntity(fields[0], fields[1], Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]), Long.parseLong(fields[5]));
        }
        return null;
    }
}
