package com.bigdata.visualanalysis.bean;

public class CategoryAverageRating {

    private String category;
    private double averageRating;

    public CategoryAverageRating() {
    }

    public CategoryAverageRating(String category, double averageRating) {
        this.category = category;
        this.averageRating = averageRating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        return "CategoryAverageRating{" +
                "category='" + category + '\'' +
                ", averageRating=" + averageRating +
                '}';
    }
}
