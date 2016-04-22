/**  
 * Project Name:solr-service  <br/>
 * File Name:MQService.java  <br/>
 * Package Name:com.zizaike.solr  <br/>
 * Date:2016年4月22日上午11:26:49  <br/>
 * Copyright (c) 2016, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.solr.service;  

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.solr.domain.event.SearchApplicationEvent;

/**  
 * ClassName:MQService <br/>  
 * Function: 发送事件. <br/>  
 * Date:     2016年4月22日 上午11:26:49 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public interface MQService {
    /**
     * 
     * sendMsg:发送消息. <br/>  
     *  
     * @author snow.zhang  
     * @param event  
     * @since JDK 1.7
     */
     void sendMsg(SearchApplicationEvent event) throws ZZKServiceException;
}
  
