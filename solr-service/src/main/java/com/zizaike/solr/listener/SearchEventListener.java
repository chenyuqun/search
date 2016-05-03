package com.zizaike.solr.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.zizaike.core.framework.event.BusinessOperation;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.solr.domain.SearchBusinessOperation;
import com.zizaike.solr.domain.event.SearchApplicationEvent;
import com.zizaike.solr.service.MQService;

/**
 * 
 * ClassName: SearchEventListener <br/>  
 * Function: search行为监听器. <br/>  
 * date: 2016年4月6日 下午7:17:15 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
@Component
public class SearchEventListener implements ApplicationListener<SearchApplicationEvent> {

    private static final Logger logger = LoggerFactory.getLogger(SearchEventListener.class);
     
    @Autowired
    private MQService searchMQService;
    @Autowired
    private MQService serviceSearchMQService;


    @Override
    public void onApplicationEvent(SearchApplicationEvent event) {
        BusinessOperation operation = event.getOperation();
        if (operation == null)
            return;
        MQService mqService = getMQService(operation);
        if (mqService != null) {
            try {
                mqService.sendMsg(event);
            } catch (ZZKServiceException e) {
                e.printStackTrace();  
                logger.error("onApplicationEvent error {}",e);
            }
        }
    }

    /**
     * 
     * getMQService:根据业务操作获取对应的事件消息服务. <br/>  
     *  
     * @author snow.zhang  
     * @param operation
     * @return  
     * @since JDK 1.7
     */
    private MQService getMQService(BusinessOperation operation) {
        MQService mqService = null;
        switch ((SearchBusinessOperation) operation) {
            case SEARCH:
                mqService = searchMQService;
                break;
            case SERVICE_SEARCH:
                mqService = serviceSearchMQService;
            default:
                break;
        }
        return mqService;
    }
}
