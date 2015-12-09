package com.zizaike.redis.service;


import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import redis.clients.jedis.Tuple;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.base.ChannelType;
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
        int rand = 0;
        for (int i = 0;i<50;i++){
            rand  = random.nextInt(10);
            searchStatisticsRedisService.zincrHotSearch(ChannelType.APP,"test"+rand);
        }
    }

    @Test(description = "查询")
    public void getTopSearch() throws ZZKServiceException {
            Set<Tuple> set = searchStatisticsRedisService.getHotSearch(ChannelType.APP);
        Iterator<Tuple> iterator = set.iterator();
        while (iterator.hasNext()){
            Tuple tuple = iterator.next();
            System.out.println(tuple.getElement()+":"+tuple.getScore());
        }
    }
}
