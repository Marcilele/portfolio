package com.bigdata.visualanalysis.bean;

public class GCDateCount {

    private String dt;
    private int count;

    public GCDateCount() {
    }

    public GCDateCount(String dt, int count) {
        this.dt = dt;
        this.count = count;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
