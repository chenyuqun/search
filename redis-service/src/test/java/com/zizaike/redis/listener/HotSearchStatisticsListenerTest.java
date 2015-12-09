package com.zizaike.redis.listener;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.SearchStatistics;
import com.zizaike.redis.basetest.BaseTest;
import com.zizaike.redis.domain.event.SearchApplicationEvent;

/**
 * 
 * ClassName: HotRecommendRedisServiceImplTest <br/>  
 * Function: 测试 <br/>  
 * date: 2015年11月10日 下午4:32:59 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
public class HotSearchStatisticsListenerTest extends BaseTest {
    @Autowired
    private ApplicationContext applicationContext;
    @Test(description = "监听测试")
    public void get() throws ZZKServiceException {
        SearchStatistics searchStatistics = new SearchStatistics();
        searchStatistics.setKeyWords("test");
        applicationContext.publishEvent(new SearchApplicationEvent(searchStatistics));
    }
}
