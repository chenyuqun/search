package com.zizaike.redis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.base.ChannelType;
import com.zizaike.entity.recommend.BNBServiceSearchStatistics;
import com.zizaike.entity.solr.BNBServiceType;
import com.zizaike.is.redis.BNBServiceStatisticsRedisService;
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
public class BNBServiceStatisticsRedisServiceImplTest extends BaseTest {
    @Autowired
    private BNBServiceStatisticsRedisService bnbServiceStatisticsRedisService;
    @Test(description = "搜索服务累加累加")
    public void zincrTopSearch() throws ZZKServiceException {
        BNBServiceSearchStatistics bnbServiceSearchStatistics = new BNBServiceSearchStatistics();
        for (int i = 0;i<100;i++){
            bnbServiceSearchStatistics = new BNBServiceSearchStatistics();
            bnbServiceSearchStatistics.setBnbServiceType(BNBServiceType.findByValue("huwai"));
            bnbServiceSearchStatistics.setChannel(ChannelType.APP);
           bnbServiceStatisticsRedisService.zincrBNBServiceSearch(bnbServiceSearchStatistics);
        }
    }

}