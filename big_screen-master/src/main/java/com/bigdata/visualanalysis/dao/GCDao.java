package com.bigdata.visualanalysis.dao;

import com.bigdata.visualanalysis.bean.*;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GCDao {

    // number of games released per year
    @Select("select yr,count from ads_year_count;")
    public List<GCYearCount> getGCYearCount();

    //Game distribution across different operating systems
    @Select("select platform,count from ads_platform_count order by count desc;")
    public List<GCPlatformCount> getGCPlatformCount();

    //different rating count
    @Select("select rating,count from ads_rating_count order by count desc;")
    public List<GCRatingCount> getGCRatingCount();

    //Top 10 most reviewed day on Steam
    @Select("select dt,count from ads_date_count order by count desc limit 10;")
    public List<GCDateCount> getGCDateCount();

    //reviews are marked as funny and helpful count
    @Select("select hfmark,count from ads_helpfulandfunny_count;")
    public List<GCHelpfulAndFunnyCount> getGCHelpfulAndFunnyCount();

    //relationship between game recommendation and the average hour user play
    @Select("select isrecommended,avghour from ads_recommended_avghour;")
    public List<GCRecommendedAndHour> getGCRecommendedAndHour();

    //Game tag count
    @Select("select replace(tag,'\"','') tag,count from ads_tag_count order by count desc;")
    public List<GCTagCount> getGCTagCount();

}
