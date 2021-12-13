package com.sun.bean;

public class AvgResourceAccumulatorEntity {
    private Double cpu;
    private Double mem;
    private Double gpu;
    private Integer count;

    public AvgResourceAccumulatorEntity() {
    }

    public AvgResourceAccumulatorEntity(Double cpu, Double mem, Double gpu, Integer count) {

        this.cpu = cpu;
        this.mem = mem;
        this.gpu = gpu;
        this.count = count;
    }



    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
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
        return "AvgResourceAccumulatorEntity{" +
                ", cpu=" + cpu +
                ", mem=" + mem +
                ", gpu=" + gpu +
                ", count=" + count +
                '}';
    }
}
