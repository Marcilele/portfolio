package com.bigdata.visualanalysis.bean;

public class GCHelpfulAndFunnyCount {

    private String hfmark;
    private int count;

    public GCHelpfulAndFunnyCount() {
    }

    public GCHelpfulAndFunnyCount(String hfmark, int count) {
        this.hfmark = hfmark;
        this.count = count;
    }

    public String getHfmark() {
        return hfmark;
    }

    public void setHfmark(String hfmark) {
        this.hfmark = hfmark;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
