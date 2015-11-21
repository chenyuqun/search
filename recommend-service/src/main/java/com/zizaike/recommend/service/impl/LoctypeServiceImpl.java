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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.Loctype;
import com.zizaike.is.recommend.LoctypeService;
import com.zizaike.recommend.dao.LoctypeDao;

/**  
 * ClassName: LoctypeServiceImpl <br/>  
 * Function: 地址impl. <br/>  
 * Reason: service. <br/>  
 * date: 2015年11月10日 下午1:55:02 <br/>  
 *  
 * @author alex  
 * @version   
 * @since JDK 1.7  
 */
@Service
public class LoctypeServiceImpl implements LoctypeService {
    private static final Logger LOG = LoggerFactory.getLogger(LoctypeServiceImpl.class);
    @Autowired
   private LoctypeDao loctypeDao;

    @Override
    public List<Loctype> queryLoctype(Loctype loctype) throws ZZKServiceException {
        long start = System.currentTimeMillis();
          List<Loctype> list = loctypeDao.queryLoctype(loctype);
          LOG.info("when call queryLoctype, use: {}ms , list .size {}", System.currentTimeMillis() - start,list.size());
        return list;
    }

    @Override
    public List<Loctype> queryByAreaLevel() throws ZZKServiceException {
          
        return loctypeDao.queryByAreaLevel();
    }
    
   
}
  
