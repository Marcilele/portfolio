package com.bigdata.visualanalysis.bean;

public class SellerCount {

    private String seller;
    private int count;

    public SellerCount() {
    }

    public SellerCount(String seller, int count) {
        this.seller = seller;
        this.count = count;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
