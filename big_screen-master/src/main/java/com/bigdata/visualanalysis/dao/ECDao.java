package com.bigdata.visualanalysis.dao;

import com.bigdata.visualanalysis.bean.*;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用来访问电商数据的接口
 */
public interface ECDao {

    //The average rating of products in different categories
    @Select("select category,average_rating averageRating from categoryaveragerating order by average_rating desc;")
    public List<CategoryAverageRating> getCategoryAverageRating();


    //The relationship between price and discount
    @Select("select dicount,count from discount order by count desc limit 20;")
    public List<Discount> getDiscount();

    //Statistics of the number of products of different brands TOP10
    @Select("select brand,count from brandcount order by count desc limit 10;")
    public List<BrandCount> getBrandCount();

    //Statistics of the number of products
    @Select("select count from productcount;")
    public ProductCount getProductCount();

    //Statistics of the number of products that are out of stock
    @Select("select count from outofstockcount;")
    public OutOfStockCount getOutOfStockCount();

    //Statistics of the number of products in different categories
    @Select("select category,count from categorycount order by count desc;")
    public List<CategoryCount> getCategoryCount();

    //Statistics of the number of products in different subcategories
    @Select("select sub_category subCategory,count from subcategorycount order by count desc limit 5;")
    public List<SubCategoryCount> getSubCategoryCount();


    //Statistics of the number of products of different sellers TOP10
    @Select("select seller,count from sellercount order by count desc limit 10;")
    public List<SellerCount> getSellerCount();

}
