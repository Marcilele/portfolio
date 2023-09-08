package com.bigdata.visualanalysis.bean;

public class SubCategoryCount {

    private String subCategory;
    private int count;

    public SubCategoryCount() {
    }

    public SubCategoryCount(String subCategory, int count) {
        this.subCategory = subCategory;
        this.count = count;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
