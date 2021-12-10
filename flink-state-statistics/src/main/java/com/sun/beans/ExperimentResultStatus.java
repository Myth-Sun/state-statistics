package com.sun.beans;

public class ExperimentResultStatus {
    private Long userId;
    private Long jobId;
    private String resultStatus;

    public ExperimentResultStatus() {
    }

    public ExperimentResultStatus(Long userId, Long jobId, String resultStatus) {
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
