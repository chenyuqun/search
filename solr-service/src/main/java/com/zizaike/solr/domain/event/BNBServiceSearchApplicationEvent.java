/**  
 * Project Name:redis-service  <br/>
 * File Name:SearchApplicationEvent.java  <br/>
 * Package Name:com.zizaike.redis.domain.event  <br/>
 * Date:2015年12月8日下午7:58:16  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.solr.domain.event;  

import org.springframework.context.ApplicationEvent;

import com.zizaike.entity.recommend.BNBServiceSearchStatistics;

/**  
 * ClassName:SearchApplicationEvent <br/>  
 * Function: 服务查询事件. <br/>  
 * Date:     2015年12月8日 下午7:58:16 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public class BNBServiceSearchApplicationEvent extends ApplicationEvent {

    private static final long serialVersionUID = -933948565885914692L;
    
    public BNBServiceSearchApplicationEvent(BNBServiceSearchStatistics searchStatistics) {
        super(searchStatistics);  
    }


}
  
