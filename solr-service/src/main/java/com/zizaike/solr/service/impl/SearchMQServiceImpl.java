package com.zizaike.solr.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zizaike.core.framework.event.CommonOperationAction;
import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.SearchStatistics;
import com.zizaike.is.redis.SearchStatisticsRedisService;
import com.zizaike.solr.domain.SearchBusinessOperation;
import com.zizaike.solr.domain.event.SearchApplicationEvent;
import com.zizaike.solr.service.MQService;

/**
 * 
 * ClassName: searchMQService <br/>  
 * Function: 查询消息（暂时只有日志）. <br/>  
 * date: 2016年4月6日 下午2:53:50 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
@Service("searchMQService")
public class SearchMQServiceImpl implements MQService {
    @Autowired
    private SearchStatisticsRedisService searchStatisticsRedisService;
    private static final Logger LOG = LoggerFactory.getLogger(SearchMQServiceImpl.class);

    @Override
    public void sendMsg(SearchApplicationEvent event) throws IllegalParamterException {
        if (SearchBusinessOperation.SEARCH != event.getOperation()) {
            return;
        }
        SearchStatistics searchStatistics = (SearchStatistics) event.getSource();
        if (CommonOperationAction.Before == event.getAction()) {
            try {
                searchStatisticsRedisService.zincrHotSearch(searchStatistics);
                searchStatisticsRedisService.zincrHotSearchEveryDay(searchStatistics);
            } catch (ZZKServiceException e) {
                e.printStackTrace();  
            }
            LOG.info("send search  before event,event source={}", searchStatistics);
        } else if (CommonOperationAction.Completed == event.getAction()) {
            try {
                searchStatisticsRedisService.zincrResultLessSearchEveryDay( searchStatistics);
                searchStatisticsRedisService.zincrResultLessSearch(searchStatistics);
            } catch (ZZKServiceException e) {
                e.printStackTrace();  
            }
            LOG.info("send search reutrn null event,event source={}", searchStatistics);
        }
    }
}
