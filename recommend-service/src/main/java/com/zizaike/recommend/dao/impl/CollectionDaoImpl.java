/**  
 * Project Name:recommend-service  <br/>
 * File Name:LoctypeDaoImpl.java  <br/>
 * Package Name:com.zizaike.recommend.dao.impl  <br/>
 * Date:2015年11月10日下午2:03:14  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/   
  
package com.zizaike.recommend.dao.impl;  

import org.springframework.stereotype.Repository;

import com.zizaike.core.framework.mybatis.impl.GenericMyIbatisDao;
import com.zizaike.entity.user.Collect;
import com.zizaike.recommend.dao.CollectDao;

@Repository
public class CollectionDaoImpl extends GenericMyIbatisDao<Collect, Integer>implements CollectDao {
    /**
     * 命名空间
     */
    private static final String NAMESPACE = "com.zizaike.recommend.dao.CollectMapper." ;

    @Override
    public String bnbCollection(Integer userId)  {
          
        return this.getSqlSession().selectOne(NAMESPACE+"bnbCollectionByUserId",userId);
    }

}
  
