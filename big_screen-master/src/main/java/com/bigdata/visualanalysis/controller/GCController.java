package com.bigdata.visualanalysis.controller;

import com.bigdata.visualanalysis.bean.*;
import com.bigdata.visualanalysis.dao.GCDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class GCController {

    @Autowired(required = false)
    private GCDao gcDao;

    //Batch Analysis - Game Recommendation on Steam
    @RequestMapping("/gcdaping")
    public String gcdaping(Model model) {

        // number of games released per year
        List<GCYearCount> yearCountList = gcDao.getGCYearCount();
        System.out.println(yearCountList);
        model.addAttribute("yearCountList",yearCountList);

        //Game distribution across different operating systems
        List<GCPlatformCount> platformCountList = gcDao.getGCPlatformCount();
        System.out.println(platformCountList);
        model.addAttribute("platformCountList",platformCountList);

        //different rating count
        List<GCRatingCount> ratingCountList = gcDao.getGCRatingCount();
        System.out.println(ratingCountList);
        model.addAttribute("ratingCountList",ratingCountList);

        //Top 10 most reviewed day on Steam
        List<GCDateCount> dateCountList = gcDao.getGCDateCount();
        System.out.println(dateCountList);
        model.addAttribute("dateCountList",dateCountList);

        //reviews are marked as funny and helpful count
        List<GCHelpfulAndFunnyCount> helpfulAndFunnyCountList = gcDao.getGCHelpfulAndFunnyCount();
        System.out.println(helpfulAndFunnyCountList);
        model.addAttribute("helpfulAndFunnyCountList",helpfulAndFunnyCountList);

        //relationship between game recommendation and the average hour user play
        List<GCRecommendedAndHour> recommendedAndHourList = gcDao.getGCRecommendedAndHour();
        System.out.println(recommendedAndHourList);
        model.addAttribute("recommendedAndHourList",recommendedAndHourList);

        //Game tag count
        List<GCTagCount> tagCountList = gcDao.getGCTagCount();
        System.out.println(tagCountList);
        model.addAttribute("tagCountList",tagCountList);

        return "gcdaping";
    }

}
