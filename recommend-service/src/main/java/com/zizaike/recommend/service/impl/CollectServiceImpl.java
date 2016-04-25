/**  
 * Project Name:recommend-service  <br/>
 * File Name:LoctypeServiceImpl.java  <br/>
 * Package Name:com.zizaike.loctype.service.impl  <br/>
 * Date:2015年11月10日下午1:55:02  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.service.impl;  

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.is.recommend.CollectService;
import com.zizaike.recommend.dao.CollectDao;

/**
 * 
 * ClassName: CollectServiceImpl <br/>  
 * Function: 收藏服务. <br/>  
 * date: 2016年4月25日 下午3:13:06 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
@Service
public class CollectServiceImpl implements CollectService {
    private static final Logger LOG = LoggerFactory.getLogger(CollectServiceImpl.class);
    @Autowired
   private CollectDao collectDao;
    @Override
    public String bnbCollection(Integer userId) throws ZZKServiceException {
          
        if(userId==null || userId<=0){
            throw new IllegalParamterException("userId is not null");
        }
        
        return collectDao.bnbCollection(userId);
    }

    
   
}
  
