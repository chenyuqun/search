/**  
 * Project Name:recommend-service  <br/>
 * File Name:DestConfigServiceImpl.java  <br/>
 * Package Name:com.zizaike.recommend.service.impl  <br/>
 * Date:2015年11月19日下午3:25:05  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.service.impl;  

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.DestConfig;
import com.zizaike.is.recommend.DestConfigService;
import com.zizaike.recommend.dao.DestConfigDao;

/**  
 * ClassName:DestConfigServiceImpl <br/>  
 * Function: 国家配置信息. <br/>  
 * Date:     2015年11月19日 下午3:25:05 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public class DestConfigServiceImpl implements DestConfigService {
    @Autowired
    private DestConfigDao destConfigDao;
    @Override
    public List<DestConfig> query() throws ZZKServiceException {
        return destConfigDao.query();
    }

}
  
