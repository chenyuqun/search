package com.zizaike.solr.listener;


import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.zizaike.core.framework.event.BusinessOperationBeforeEvent;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.base.ChannelType;
import com.zizaike.entity.recommend.SearchStatistics;
import com.zizaike.solr.basetest.BaseTest;
import com.zizaike.solr.bo.EventPublishService;
import com.zizaike.solr.domain.SearchBusinessOperation;

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
    private EventPublishService eventPublishService;
    @Test(description = "监听测试")
    public void get() throws ZZKServiceException {
        SearchStatistics searchStatistics = new SearchStatistics();
        searchStatistics.setKeyWords("test");
        searchStatistics.setChannel(ChannelType.APP);
        //发布热搜统计
        BusinessOperationBeforeEvent<SearchStatistics> beforeEvent = new BusinessOperationBeforeEvent<SearchStatistics>(
                SearchBusinessOperation.SEARCH, searchStatistics);
        eventPublishService.publishEvent(beforeEvent);
    }
}
