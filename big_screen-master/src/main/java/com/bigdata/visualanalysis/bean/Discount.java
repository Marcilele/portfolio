package com.bigdata.visualanalysis.bean;

public class Discount {

    private String dicount;
    private int count;

    public Discount() {
    }

    public Discount(String dicount, int count) {
        this.dicount = dicount;
        this.count = count;
    }


    public String getDicount() {
        return dicount;
    }

    public void setDicount(String dicount) {
        this.dicount = dicount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
