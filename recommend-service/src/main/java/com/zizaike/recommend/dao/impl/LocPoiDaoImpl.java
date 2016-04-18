/**  
 * Project Name:recommend-service  <br/>
 * File Name:LoctypeDaoImpl.java  <br/>
 * Package Name:com.zizaike.recommend.dao.impl  <br/>
 * Date:2015年11月10日下午2:03:14  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/   
  
package com.zizaike.recommend.dao.impl;  

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.core.framework.mybatis.impl.GenericMyIbatisDao;
import com.zizaike.entity.recommend.LocPoi;
import com.zizaike.recommend.dao.LocPoiDao;

@Repository
public class LocPoiDaoImpl extends GenericMyIbatisDao<LocPoi, Integer>implements LocPoiDao {
    /**
     * 命名空间
     */
    private static final String NAMESPACE = "com.zizaike.recommend.dao.LocPoiMapper." ;

    @Override
    public List<LocPoi> queryLocPoi(String poiName) throws ZZKServiceException {
          
        return this.getSqlSession().selectList(NAMESPACE+"queryLocPoiByPoiName",poiName);
    }

}
  
