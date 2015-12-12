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
import com.zizaike.solr.domain.event.ResultLessSearchApplicationEvent;

/**  
 * ClassName:HotSearchStatisticsListener <br/>  
 * Function: 无结果统计. <br/>  
 * Date:     2015年12月8日 下午8:40:59 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
@Component
public class ResultLessSearchStatisticsListener implements ApplicationListener<ResultLessSearchApplicationEvent>{
    @Autowired
    private SearchStatisticsRedisService searchStatisticsRedisService;
    @Async
    @Override
    public void onApplicationEvent(ResultLessSearchApplicationEvent event) {
          
        SearchStatistics searchStatistics =  (SearchStatistics) event.getSource();
        try {
            searchStatisticsRedisService.zincrResultLessSearch(searchStatistics);
        } catch (ZZKServiceException e) {
            e.printStackTrace();  
        }
    }

}
  
