package com.bigdata.visualanalysis.dao;

import com.bigdata.visualanalysis.bean.*;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用来访问电商数据的接口
 */
public interface ECDao {

    //不同类别的产品的平均评分
    @Select("select category,average_rating averageRating from categoryaveragerating order by average_rating desc;")
    public List<CategoryAverageRating> getCategoryAverageRating();

    //价格与折扣的关系
    @Select("select actual_price actualPrice,discount from actualpriceanddiscount order by actualPrice desc limit 20;")
    public List<ActualPriceAndDiscount> getActualPriceAndDiscount();

    //统计不同品牌的产品数量TOP10
    @Select("select brand,count from brandcount order by count desc limit 10;")
    public List<BrandCount> getBrandCount();

    //统计产品数量
    @Select("select count from productcount;")
    public ProductCount getProductCount();

    //统计有多少产品处于缺货状态
    @Select("select count from outofstockcount;")
    public OutOfStockCount getOutOfStockCount();

    //统计不同类别的产品数量
    @Select("select category,count from categorycount order by count desc;")
    public List<CategoryCount> getCategoryCount();

    //统计子类别的产品数量TOP10
    @Select("select sub_category subCategory,count from subcategorycount order by count desc limit 5;")
    public List<SubCategoryCount> getSubCategoryCount();

    //统计不同卖家的产品数量TOP10
    @Select("select seller,count from sellercount order by count desc limit 10;")
    public List<SellerCount> getSellerCount();

}
