package com.bigdata.visualanalysis.bean;

public class ActualPriceAndDiscount {

    private int actualPrice;
    private double discount;

    public ActualPriceAndDiscount() {
    }

    public ActualPriceAndDiscount(int actualPrice, double discount) {
        this.actualPrice = actualPrice;
        this.discount = discount;
    }

    public int getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(int actualPrice) {
        this.actualPrice = actualPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

}
