package com.sun.bean;

import com.sun.util.DateFormatTrans;

public class AvgResourceEntity {
    private String userId;
    private Double cpu;
    private Double mem;
    private Double gpu;
    private Integer count;
    private Long windowEnd;

    public AvgResourceEntity() {
    }

    public AvgResourceEntity(String userId, Double cpu, Double mem, Double gpu, Integer count, Long windowEnd) {
        this.userId = userId;
        this.cpu = cpu;
        this.mem = mem;
        this.gpu = gpu;
        this.count = count;
        this.windowEnd = windowEnd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Long getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(Long windowEnd) {
        this.windowEnd = windowEnd;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AvgResourceEntity{" +
                "userId='" + userId + '\'' +
                ", cpu=" + cpu +
                ", mem=" + mem +
                ", gpu=" + gpu +
                ", count=" + count +
                ", windowEnd=" + DateFormatTrans.timestampToString(String.valueOf(windowEnd)) +
                '}';
    }
}
