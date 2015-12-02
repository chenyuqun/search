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
import com.zizaike.entity.recommend.Loctype;
import com.zizaike.entity.recommend.TopRecommend;
import com.zizaike.recommend.dao.TopRecommendDao;


@Repository
public class TopRecommendDaoImpl extends GenericMyIbatisDao<Loctype, Integer>implements TopRecommendDao {
    /**
     * 命名空间
     */
    private static final String NAMESPACE = "com.zizaike.entity.recommend.dao.TopRecommendMapper." ;

    @Override
    public List<TopRecommend> quryAll() throws ZZKServiceException {
          
        return this.getSqlSession().selectList(NAMESPACE+"quryAll");
    }

}
  
