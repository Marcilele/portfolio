package com.bigdata.visualanalysis.bean;

public class GCPlatformCount {

    private String platform;
    private int count;

    public GCPlatformCount() {
    }

    public GCPlatformCount(String platform, int count) {
        this.platform = platform;
        this.count = count;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
