package com.bigdata.visualanalysis.bean;

public class GCYearCount {

    private String yr;
    private int count;

    public GCYearCount() {
    }

    public GCYearCount(String yr, int count) {
        this.yr = yr;
        this.count = count;
    }

    public String getYr() {
        return yr;
    }

    public void setYr(String yr) {
        this.yr = yr;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
