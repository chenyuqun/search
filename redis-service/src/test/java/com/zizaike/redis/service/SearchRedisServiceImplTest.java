package com.zizaike.redis.service;


import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.base.ChannelType;
import com.zizaike.entity.recommend.SearchStatistics;
import com.zizaike.is.redis.SearchStatisticsRedisService;
import com.zizaike.redis.basetest.BaseTest;

/**
 * 
 * ClassName: HotRecommendRedisServiceImplTest <br/>  
 * Function: 热点+行政<br/>  
 * date: 2015年11月10日 下午4:32:59 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
public class SearchRedisServiceImplTest extends BaseTest {
    @Autowired
    private SearchStatisticsRedisService searchStatisticsRedisService;
    @Test(description = "增加搜索累加")
    public void zincrTopSearch() throws ZZKServiceException {
        Random random = new Random();
        SearchStatistics searchStatistics = new SearchStatistics();
        int rand = 0;
        for (int i = 0;i<100;i++){
            rand  = random.nextInt(1000);
            searchStatistics = new SearchStatistics();
           searchStatistics.setLocId(1111+rand);
           searchStatistics.setPoiId(2222+rand);
           searchStatistics.setKeyWords("测试"+rand);
           searchStatistics.setChannel(ChannelType.APP);
            searchStatisticsRedisService.zincrHotSearch(searchStatistics);
        }
    }

    @Test(description = "查询")
    public void getTopSearch() throws ZZKServiceException {
            List<SearchStatistics> list = searchStatisticsRedisService.getHotSearch(ChannelType.APP);
        Iterator<SearchStatistics> iterator = list.iterator();
        while (iterator.hasNext()){
            SearchStatistics t = iterator.next();
            System.out.println(t.toString());
        }
    }
}