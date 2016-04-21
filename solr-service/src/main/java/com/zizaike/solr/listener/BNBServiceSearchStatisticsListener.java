/**  
 * Project Name:redis-service  <br/>
 * File Name:HotSearchStatisticsListener.java  <br/>
 * Package Name:com.zizaike.redis.listener  <br/>
 * Date:2015年12月8日下午8:40:59  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.solr.listener;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.BNBServiceSearchStatistics;
import com.zizaike.is.redis.BNBServiceStatisticsRedisService;
import com.zizaike.solr.domain.event.ResultLessSearchApplicationEvent;

/**
 * 
 * ClassName: BNBServiceSearchStatisticsListener <br/>  
 * Function: bnb服务查询统计. <br/>  
 * date: 2016年4月20日 下午5:21:57 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
@Component
public class BNBServiceSearchStatisticsListener implements ApplicationListener<ResultLessSearchApplicationEvent>{
    @Autowired
    private BNBServiceStatisticsRedisService bnbServiceStatisticsRedisService;
    @Async
    @Override
    public void onApplicationEvent(ResultLessSearchApplicationEvent event) {
          
        BNBServiceSearchStatistics bnbServiceSearchStatistics =  (BNBServiceSearchStatistics) event.getSource();
        try {
            bnbServiceStatisticsRedisService.zincrBNBServiceSearch(bnbServiceSearchStatistics);
        } catch (ZZKServiceException e) {
            e.printStackTrace();  
        }
    }

}
  
