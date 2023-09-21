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

    //Average rating of products in different categories
    @RequestMapping("/categoryaveragerating")
    @ResponseBody
    public Object categoryaveragerating(Model model) {
//        List<CategoryAverageRating> categoryAverageRatingList = ecDao.getCategoryAverageRating();
//        System.out.println(JSON.toJSON(categoryAverageRatingList));
////		model.addAttribute("categoryAverageRatingList",categoryAverageRatingList);
//
//        return JSON.toJSON(categoryAverageRatingList);

    //Read data from redis
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


    //Sort
        Collections.sort(categoryAverageRatingList, (a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()));
        System.out.println(JSON.toJSON(categoryAverageRatingList));

        return JSON.toJSON(categoryAverageRatingList);

    }

    //calculate the total number of products with different discounts, sort by product count, and take the top 20 most used discounts
    @RequestMapping("/discountcount")
    @ResponseBody
    public Object discount(Model model) {
//        List<Discount> discountList = ecDao.getActualPriceAndDiscount();
//        System.out.println(JSON.toJSON(discountList));
////		model.addAttribute("discountList",discountList);
//
//        return JSON.toJSON(discountList);

        Jedis jedis = RedisUtil.getJedis();
        Set<String> keys = jedis.hkeys("discount");
        List<Discount> discountList = new ArrayList<Discount>();
        for (String key : keys) {
            String value = jedis.hget("discount", key);
            Discount discount = new Discount();
            discount.setDicount(key);
            discount.setCount(Integer.parseInt(value));
            discountList.add(discount);
        }

        // sort
        Collections.sort(discountList, (a, b) -> Integer.compare(b.getCount(), a.getCount()));

        // get top 20
        List<Discount> top20List = discountList.subList(0, Math.min(20, discountList.size()));

        System.out.println(JSON.toJSON(top20List));

        return JSON.toJSON(top20List);

    }

    //calculate the top 10 brands with the most products
    @RequestMapping("/brandcount")
    @ResponseBody
    public Object brandcount(Model model) {
//        List<BrandCount> brandCountList = ecDao.getBrandCount();
//        System.out.println(JSON.toJSON(brandCountList));
////		model.addAttribute("brandCountList",brandCountList);
//
//        return JSON.toJSON(brandCountList);

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

        // sort
        Collections.sort(brandCountList, (a, b) -> Integer.compare(b.getCount(), a.getCount()));

        // get top 10
        List<BrandCount> top10List = brandCountList.subList(0, Math.min(10, brandCountList.size()));

        System.out.println(JSON.toJSON(top10List));

        return JSON.toJSON(top10List);

    }

    //calculate the total number of products
    @RequestMapping("/productcount")
    @ResponseBody
    public Object productcount(Model model) {
//        ProductCount productCount = ecDao.getProductCount();
//        System.out.println(JSON.toJSON(productCount));
////		model.addAttribute("productCount",productCount);
//
//        return JSON.toJSON(productCount);

        Jedis jedis = RedisUtil.getJedis();
        String value = jedis.hget("productcount", "productCount");
        ProductCount productCount = new ProductCount();
        productCount.setCount(Integer.parseInt(value));

        System.out.println(JSON.toJSON(productCount));
        return JSON.toJSON(productCount);

    }

    //calculate the total number of products that are out of stock
    @RequestMapping("/outofstockcount")
    @ResponseBody
    public Object outofstockcount(Model model) {
//        OutOfStockCount outOfStockCount = ecDao.getOutOfStockCount();
//        System.out.println(JSON.toJSON(outOfStockCount));
////		model.addAttribute("outOfStockCount",outOfStockCount);
//
//        return JSON.toJSON(outOfStockCount);

        Jedis jedis = RedisUtil.getJedis();
        String value = jedis.hget("outofstockcount", "TRUE");
        OutOfStockCount outOfStockCount = new OutOfStockCount();
        outOfStockCount.setCount(Integer.parseInt(value));

        System.out.println(JSON.toJSON(outOfStockCount));
        return JSON.toJSON(outOfStockCount);

    }

    //calculate the total number of products that are in different categories
    @RequestMapping("/categorycount")
    @ResponseBody
    public Object categorycount(Model model) {
//        List<CategoryCount> categoryCountList = ecDao.getCategoryCount();
//        System.out.println(JSON.toJSON(categoryCountList));
////		model.addAttribute("categoryCountList",categoryCountList);
//
//        return JSON.toJSON(categoryCountList);

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

        // sort
        Collections.sort(categoryCountList, (a, b) -> Double.compare(b.getCount(), a.getCount()));
        System.out.println(JSON.toJSON(categoryCountList));

        return JSON.toJSON(categoryCountList);

    }

    //calculate the total number of products that are in different subcategories, take the top 5 most subcategories
    @RequestMapping("/subcategorycount")
    @ResponseBody
    public Object subcategorycount(Model model) {
//        List<SubCategoryCount> subCategoryCountList = ecDao.getSubCategoryCount();
//        System.out.println(JSON.toJSON(subCategoryCountList));
////		model.addAttribute("subCategoryCountList",subCategoryCountList);
//
//        return JSON.toJSON(subCategoryCountList);

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

        // sort
        Collections.sort(subCategoryCountList, (a, b) -> Integer.compare(b.getCount(), a.getCount()));

        // take top 5
        List<SubCategoryCount> top5List = subCategoryCountList.subList(0, Math.min(5, subCategoryCountList.size()));

        System.out.println(JSON.toJSON(top5List));

        return JSON.toJSON(top5List);

    }

    //Calculate the number of products with different sellers, sort by product count, and take the top 10 sellers with the most products
    @RequestMapping("/sellercount")
    @ResponseBody
    public Object sellercount(Model model) {
//        List<SellerCount> sellerCountList = ecDao.getSellerCount();
//        System.out.println(JSON.toJSON(sellerCountList));
////		model.addAttribute("sellerCountList",sellerCountList);
//
//        return JSON.toJSON(sellerCountList);

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

        // sort
        Collections.sort(sellerCountList, (a, b) -> Integer.compare(b.getCount(), a.getCount()));

        // take top 10
        List<SellerCount> top10List = sellerCountList.subList(0, Math.min(10, sellerCountList.size()));

        System.out.println(JSON.toJSON(top10List));

        return JSON.toJSON(top10List);

    }

}
