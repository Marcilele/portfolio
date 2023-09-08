package com.bigdata.visualanalysis.controller;

import com.alibaba.fastjson.JSON;
import com.bigdata.visualanalysis.bean.*;
import com.bigdata.visualanalysis.dao.ECDao;
import com.bigdata.visualanalysis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class ECController {

    @Autowired(required = false)
    private ECDao ecDao;

    @RequestMapping("/ecdaping")
    public String ecindex(Model model) {
        return "index";
    }

    //不同类别的产品的平均评分
    @RequestMapping("/categoryaveragerating")
    @ResponseBody
    public Object categoryaveragerating(Model model) {
//        List<CategoryAverageRating> categoryAverageRatingList = ecDao.getCategoryAverageRating();
//        System.out.println(JSON.toJSON(categoryAverageRatingList));
////		model.addAttribute("categoryAverageRatingList",categoryAverageRatingList);
//
//        return JSON.toJSON(categoryAverageRatingList);

        //从redis读取数据
        Jedis jedis = RedisUtil.getJedis();
        Set<String> keys = jedis.hkeys("categoryaveragerating");
        List<CategoryAverageRating> categoryAverageRatingList = new ArrayList<CategoryAverageRating>();
        for (String key : keys) {
            String value = jedis.hget("categoryaveragerating", key);
            CategoryAverageRating categoryAverageRating = new CategoryAverageRating();
            categoryAverageRating.setCategory(key);
            categoryAverageRating.setAverageRating(Double.parseDouble(value));
            categoryAverageRatingList.add(categoryAverageRating);
        }

        // 排序
        Collections.sort(categoryAverageRatingList, (a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()));
        System.out.println(JSON.toJSON(categoryAverageRatingList));

        return JSON.toJSON(categoryAverageRatingList);

    }

    //价格与折扣的关系
    @RequestMapping("/actualpriceanddiscount")
    @ResponseBody
    public Object actualpriceanddiscount(Model model) {
//        List<ActualPriceAndDiscount> actualPriceAndDiscountList = ecDao.getActualPriceAndDiscount();
//        System.out.println(JSON.toJSON(actualPriceAndDiscountList));
////		model.addAttribute("actualPriceAndDiscountList",actualPriceAndDiscountList);
//
//        return JSON.toJSON(actualPriceAndDiscountList);

        //从redis读取数据
        Jedis jedis = RedisUtil.getJedis();
        Set<String> keys = jedis.hkeys("actualpriceanddiscount");
        List<ActualPriceAndDiscount> actualPriceAndDiscountList = new ArrayList<ActualPriceAndDiscount>();
        for (String key : keys) {
            String value = jedis.hget("actualpriceanddiscount", key);
            ActualPriceAndDiscount actualPriceAndDiscount = new ActualPriceAndDiscount();
            actualPriceAndDiscount.setActualPrice(Integer.parseInt(key));
            actualPriceAndDiscount.setDiscount(Double.parseDouble(value));
            actualPriceAndDiscountList.add(actualPriceAndDiscount);
        }

        // 排序
        Collections.sort(actualPriceAndDiscountList, (a, b) -> Integer.compare(b.getActualPrice(), a.getActualPrice()));

        // 取前20个
        List<ActualPriceAndDiscount> top20List = actualPriceAndDiscountList.subList(0, Math.min(20, actualPriceAndDiscountList.size()));

        System.out.println(JSON.toJSON(top20List));

        return JSON.toJSON(top20List);

    }

    //统计不同品牌的产品数量TOP10
    @RequestMapping("/brandcount")
    @ResponseBody
    public Object brandcount(Model model) {
//        List<BrandCount> brandCountList = ecDao.getBrandCount();
//        System.out.println(JSON.toJSON(brandCountList));
////		model.addAttribute("brandCountList",brandCountList);
//
//        return JSON.toJSON(brandCountList);

        //从redis读取数据
        Jedis jedis = RedisUtil.getJedis();
        Set<String> keys = jedis.hkeys("brandcount");
        List<BrandCount> brandCountList = new ArrayList<BrandCount>();
        for (String key : keys) {
            String value = jedis.hget("brandcount", key);
            BrandCount brandCount = new BrandCount();
            brandCount.setBrand(key);
            brandCount.setCount(Integer.parseInt(value));
            brandCountList.add(brandCount);
        }

        // 排序
        Collections.sort(brandCountList, (a, b) -> Integer.compare(b.getCount(), a.getCount()));

        // 取前10个
        List<BrandCount> top10List = brandCountList.subList(0, Math.min(10, brandCountList.size()));

        System.out.println(JSON.toJSON(top10List));

        return JSON.toJSON(top10List);

    }

    //统计产品数量
    @RequestMapping("/productcount")
    @ResponseBody
    public Object productcount(Model model) {
//        ProductCount productCount = ecDao.getProductCount();
//        System.out.println(JSON.toJSON(productCount));
////		model.addAttribute("productCount",productCount);
//
//        return JSON.toJSON(productCount);

        //从redis读取数据
        Jedis jedis = RedisUtil.getJedis();
        String value = jedis.hget("productcount", "productCount");
        ProductCount productCount = new ProductCount();
        productCount.setCount(Integer.parseInt(value));

        System.out.println(JSON.toJSON(productCount));
        return JSON.toJSON(productCount);

    }

    //统计有多少产品处于缺货状态
    @RequestMapping("/outofstockcount")
    @ResponseBody
    public Object outofstockcount(Model model) {
//        OutOfStockCount outOfStockCount = ecDao.getOutOfStockCount();
//        System.out.println(JSON.toJSON(outOfStockCount));
////		model.addAttribute("outOfStockCount",outOfStockCount);
//
//        return JSON.toJSON(outOfStockCount);

        //从redis读取数据
        Jedis jedis = RedisUtil.getJedis();
        String value = jedis.hget("outofstockcount", "TRUE");
        OutOfStockCount outOfStockCount = new OutOfStockCount();
        outOfStockCount.setCount(Integer.parseInt(value));

        System.out.println(JSON.toJSON(outOfStockCount));
        return JSON.toJSON(outOfStockCount);

    }

    //统计不同类别的产品数量
    @RequestMapping("/categorycount")
    @ResponseBody
    public Object categorycount(Model model) {
//        List<CategoryCount> categoryCountList = ecDao.getCategoryCount();
//        System.out.println(JSON.toJSON(categoryCountList));
////		model.addAttribute("categoryCountList",categoryCountList);
//
//        return JSON.toJSON(categoryCountList);

        //从redis读取数据
        Jedis jedis = RedisUtil.getJedis();
        Set<String> keys = jedis.hkeys("categorycount");
        List<CategoryCount> categoryCountList = new ArrayList<CategoryCount>();
        for (String key : keys) {
            String value = jedis.hget("categorycount", key);
            CategoryCount categoryCount = new CategoryCount();
            categoryCount.setCategory(key);
            categoryCount.setCount(Integer.parseInt(value));
            categoryCountList.add(categoryCount);
        }

        // 排序
        Collections.sort(categoryCountList, (a, b) -> Double.compare(b.getCount(), a.getCount()));
        System.out.println(JSON.toJSON(categoryCountList));

        return JSON.toJSON(categoryCountList);

    }

    //统计子类别的产品数量TOP5
    @RequestMapping("/subcategorycount")
    @ResponseBody
    public Object subcategorycount(Model model) {
//        List<SubCategoryCount> subCategoryCountList = ecDao.getSubCategoryCount();
//        System.out.println(JSON.toJSON(subCategoryCountList));
////		model.addAttribute("subCategoryCountList",subCategoryCountList);
//
//        return JSON.toJSON(subCategoryCountList);

        //从redis读取数据
        Jedis jedis = RedisUtil.getJedis();
        Set<String> keys = jedis.hkeys("subcategorycount");
        List<SubCategoryCount> subCategoryCountList = new ArrayList<SubCategoryCount>();
        for (String key : keys) {
            String value = jedis.hget("subcategorycount", key);
            SubCategoryCount subCategoryCount = new SubCategoryCount();
            subCategoryCount.setSubCategory(key);
            subCategoryCount.setCount(Integer.parseInt(value));
            subCategoryCountList.add(subCategoryCount);
        }

        // 排序
        Collections.sort(subCategoryCountList, (a, b) -> Integer.compare(b.getCount(), a.getCount()));

        // 取前5个
        List<SubCategoryCount> top5List = subCategoryCountList.subList(0, Math.min(5, subCategoryCountList.size()));

        System.out.println(JSON.toJSON(top5List));

        return JSON.toJSON(top5List);

    }

    //统计不同卖家的产品数量TOP10
    @RequestMapping("/sellercount")
    @ResponseBody
    public Object sellercount(Model model) {
//        List<SellerCount> sellerCountList = ecDao.getSellerCount();
//        System.out.println(JSON.toJSON(sellerCountList));
////		model.addAttribute("sellerCountList",sellerCountList);
//
//        return JSON.toJSON(sellerCountList);

        //从redis读取数据
        Jedis jedis = RedisUtil.getJedis();
        Set<String> keys = jedis.hkeys("sellercount");
        List<SellerCount> sellerCountList = new ArrayList<SellerCount>();
        for (String key : keys) {
            String value = jedis.hget("sellercount", key);
            SellerCount sellerCount = new SellerCount();
            sellerCount.setSeller(key);
            sellerCount.setCount(Integer.parseInt(value));
            sellerCountList.add(sellerCount);
        }

        // 排序
        Collections.sort(sellerCountList, (a, b) -> Integer.compare(b.getCount(), a.getCount()));

        // 取前10个
        List<SellerCount> top10List = sellerCountList.subList(0, Math.min(10, sellerCountList.size()));

        System.out.println(JSON.toJSON(top10List));

        return JSON.toJSON(top10List);

    }

}
