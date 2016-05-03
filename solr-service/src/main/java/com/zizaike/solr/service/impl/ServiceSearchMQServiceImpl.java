package com.zizaike.solr.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zizaike.core.framework.event.CommonOperationAction;
import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.BNBServiceSearchStatistics;
import com.zizaike.is.redis.BNBServiceStatisticsRedisService;
import com.zizaike.solr.domain.SearchBusinessOperation;
import com.zizaike.solr.domain.event.SearchApplicationEvent;
import com.zizaike.solr.service.MQService;

/**
 * 
 * ClassName: serviceSearchMQService <br/>  
 * Function: 查询消息（暂时只有日志）. <br/>  
 * date: 2016年4月6日 下午2:53:50 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
@Service("serviceSearchMQService")
public class ServiceSearchMQServiceImpl implements MQService {
    @Autowired
    private BNBServiceStatisticsRedisService bnbServiceStatisticsRedisService;
    private static final Logger LOG = LoggerFactory.getLogger(ServiceSearchMQServiceImpl.class);

    @Override
    public void sendMsg(SearchApplicationEvent event) throws IllegalParamterException {
        if (SearchBusinessOperation.SERVICE_SEARCH != event.getOperation()) {
            return;
        }
        BNBServiceSearchStatistics bnbServiceSearchStatistics =  (BNBServiceSearchStatistics) event.getSource();
        if (CommonOperationAction.Before == event.getAction()) {
            try {
                bnbServiceStatisticsRedisService.zincrBNBServiceSearch(bnbServiceSearchStatistics);
            } catch (ZZKServiceException e) {
                e.printStackTrace();  
            }
            LOG.info("send service search  before event,event source={}", bnbServiceSearchStatistics);
        } else if (CommonOperationAction.Failed == event.getAction()) {
            LOG.info("send service reutrn null event,event source={}", bnbServiceSearchStatistics);
        }
    }
}
