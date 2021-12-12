package com.sun.map;

import com.sun.bean.UserJobInfoEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

public class UserJobInfoMapFunction implements MapFunction<String, UserJobInfoEntity> {
    @Override
    public UserJobInfoEntity map(String value) throws Exception {
        if (StringUtils.isNotEmpty(value)) {
            String[] fields = value.split(",");
            return new UserJobInfoEntity(fields[0], Long.valueOf(fields[1]), fields[2], Long.valueOf(fields[3]));
        }
        return null;
    }
}
