/**  
 * Project Name:recommend-service  <br/>
 * File Name:LoctypeServiceImpl.java  <br/>
 * Package Name:com.zizaike.loctype.service.impl  <br/>
 * Date:2015年11月10日下午1:55:02  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.service.impl;  

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.LocPoi;
import com.zizaike.is.recommend.LocPoiService;
import com.zizaike.recommend.dao.LocPoiDao;

/**
 * 
 * ClassName: LoctPoiServiceImpl <br/>  
 * Function: poi. <br/>  
 * date: 2016年4月18日 下午3:52:12 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
@Service
public class LocPoiServiceImpl implements LocPoiService {
    @Autowired
   private LocPoiDao locPoiDao;
    @Override
    public List<LocPoi> queryLocPoi(String poiName) throws ZZKServiceException {
        if(StringUtils.isEmpty(poiName)){
            throw new IllegalParamterException("poiName is no empty");
        }
        return locPoiDao.queryLocPoi(poiName);
    }


    
   
}
  
