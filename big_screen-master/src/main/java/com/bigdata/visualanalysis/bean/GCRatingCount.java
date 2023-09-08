package com.bigdata.visualanalysis.bean;

public class GCRatingCount {

    private String rating;
    private int count;

    public GCRatingCount() {
    }

    public GCRatingCount(String rating, int count) {
        this.rating = rating;
        this.count = count;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
