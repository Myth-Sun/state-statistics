package com.sun.bean;

import java.io.Serializable;

public class JobResourceInfoEntity implements Serializable {
    private String userId;
    private String jobId;
    private Double cpu;
    private Double mem;
    private Double gpu;
    private Long timestamp;

    public JobResourceInfoEntity() {
    }

    public JobResourceInfoEntity(String userId, String jobId, Double cpu, Double mem, Double gpu, Long timestamp) {
        this.userId = userId;
        this.jobId = jobId;
        this.cpu = cpu;
        this.mem = mem;
        this.gpu = gpu;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getMem() {
        return mem;
    }

    public void setMem(Double mem) {
        this.mem = mem;
    }

    public Double getGpu() {
        return gpu;
    }

    public void setGpu(Double gpu) {
        this.gpu = gpu;
    }

    @Override
    public String toString() {
        return "JobResourceInfoEntity{" +
                "userId='" + userId + '\'' +
                ", jobId='" + jobId + '\'' +
                ", cpu=" + cpu +
                ", mem=" + mem +
                ", gpu=" + gpu +
                ", timestamp=" + timestamp +
                '}';
    }
}
