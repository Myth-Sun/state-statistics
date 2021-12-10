package com.sun.bean;

public class ExperimentEntity {
    private Long userId;
    private Long jobId;
    private String resultStatus;

    public ExperimentEntity() {
    }

    public ExperimentEntity(Long userId, Long jobId, String resultStatus) {
        this.userId = userId;
        this.jobId = jobId;
        this.resultStatus = resultStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    @Override
    public String toString() {
        return "ExperimentResultStatus{" +
                "userId=" + userId +
                ", jobId=" + jobId +
                ", resultStatus='" + resultStatus + '\'' +
                '}';
    }
}
