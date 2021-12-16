package com.example.job.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.sun.bean.UserJobInfoEntity;
import com.sun.interfaces.JobInfoService;
import org.springframework.stereotype.Component;

@Service
@Component
public class JobInfoServiceImpl implements JobInfoService {
    public UserJobInfoEntity getUserJobInfo(String userId, Long jobId) {
        return new UserJobInfoEntity(userId, jobId, "RUNNING", System.currentTimeMillis());
    }
}
