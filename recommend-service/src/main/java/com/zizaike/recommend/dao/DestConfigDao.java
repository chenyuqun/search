/**  
 * Project Name:recommend-service  <br/>
 * File Name:DestConfigDao.java  <br/>
 * Package Name:com.zizaike.recommend  <br/>
 * Date:2015年11月19日下午3:13:03  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.dao;  

import java.util.List;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.core.framework.springext.database.Slave;
import com.zizaike.entity.recommend.DestConfig;

/**  
 * ClassName:DestConfigDao <br/>  
 * Function: 国家配置信息. <br/>  
 * Date:     2015年11月19日 下午3:13:03 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public interface DestConfigDao {
    /**
     * 
     * query:查询所有国家信息. <br/>  
     *  
     * @author snow.zhang  
     * @return
     * @throws ZZKServiceException  
     * @since JDK 1.7
     */
    @Slave
    List<DestConfig> query() throws ZZKServiceException;
    
}
  
