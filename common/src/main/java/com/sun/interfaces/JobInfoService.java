package com.sun.interfaces;

import com.sun.bean.UserJobInfoEntity;

public interface JobInfoService {
    UserJobInfoEntity getUserJobInfo(String userId, Long jobId);
}
