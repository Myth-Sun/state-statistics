package com.sun.bean;

import java.io.Serializable;

public class UserJobInfoEntity implements Serializable {
    private String userId;
    private Long jobId;
    private String jobStatus;
    private Long timestamp;

    public UserJobInfoEntity() {
    }

    public UserJobInfoEntity(String userId, Long jobId, String jobStatus, Long timestamp) {
        this.userId = userId;
        this.jobId = jobId;
        this.jobStatus = jobStatus;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserJobInfoEntity{" +
                "userId=" + userId +
                ", jobId=" + jobId +
                ", jobStatus='" + jobStatus + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
