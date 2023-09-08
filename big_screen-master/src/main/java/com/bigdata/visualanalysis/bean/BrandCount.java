package com.bigdata.visualanalysis.bean;

public class BrandCount {

    private String brand;
    private int count;

    public BrandCount() {
    }

    public BrandCount(String brand, int count) {
        this.brand = brand;
        this.count = count;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
