/**  
 * Project Name:recommend-service  <br/>
 * File Name:DestConfigDaoImpl.java  <br/>
 * Package Name:com.zizaike.recommend.dao.impl  <br/>
 * Date:2015年11月19日下午3:16:16  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.dao.impl;  

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.core.framework.mybatis.impl.GenericMyIbatisDao;
import com.zizaike.entity.recommend.DestConfig;
import com.zizaike.recommend.dao.DestConfigDao;

/**  
 * ClassName:DestConfigDaoImpl <br/>  
 * Function: 国家配置. <br/>  
 * Date:     2015年11月19日 下午3:16:16 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
@Repository
public class DestConfigDaoImpl extends GenericMyIbatisDao<DestConfig, Integer> implements DestConfigDao{
    private static final String NAMESPACE = "com.zizaike.recommend.dao.DestConfigMapper." ;
    @Override
    public List<DestConfig> query() throws ZZKServiceException {
        return this.getSqlSession().selectList(NAMESPACE+"queryAll");
    }

}
  
