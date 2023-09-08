package com.bigdata.visualanalysis.bean;

public class GCRecommendedAndHour {

    private String isrecommended;
    private double avghour;

    public GCRecommendedAndHour() {
    }

    public GCRecommendedAndHour(String isrecommended, double avghour) {
        this.isrecommended = isrecommended;
        this.avghour = avghour;
    }

    public String getIsrecommended() {
        return isrecommended;
    }

    public void setIsrecommended(String isrecommended) {
        this.isrecommended = isrecommended;
    }

    public double getAvghour() {
        return avghour;
    }

    public void setAvghour(double avghour) {
        this.avghour = avghour;
    }

}
