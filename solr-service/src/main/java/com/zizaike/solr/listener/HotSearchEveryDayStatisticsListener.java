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
import com.zizaike.entity.recommend.SearchStatistics;
import com.zizaike.is.redis.SearchStatisticsRedisService;
import com.zizaike.solr.domain.event.HotSearchApplicationEvent;

/**  
 * ClassName:HotSearchStatisticsListener <br/>  
 * Function: 热搜统计. <br/>  
 * Date:     2015年12月8日 下午8:40:59 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
@Component
public class HotSearchEveryDayStatisticsListener implements ApplicationListener<HotSearchApplicationEvent>{
    @Autowired
    private SearchStatisticsRedisService searchStatisticsRedisService;
    @Async
    @Override
    public void onApplicationEvent(HotSearchApplicationEvent event) {
          
        SearchStatistics searchStatistics =  (SearchStatistics) event.getSource();
        try {
            searchStatisticsRedisService.zincrHotSearchEveryDay(searchStatistics);
        } catch (ZZKServiceException e) {
            e.printStackTrace();  
        }
    }

}
  
