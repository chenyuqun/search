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
import com.zizaike.entity.recommend.hot.Loctype;
import com.zizaike.entity.recommend.hot.Recommend;
import com.zizaike.recommend.dao.HotRecommendDao;
import com.zizaike.recommend.dao.LoctypeDao;

/**  
 * ClassName: LoctypeDaoImpl <br/>  
 * Function: 地址Dao. <br/>  
 * date: 2015年11月10日 下午2:03:14 <br/>  
 *  
 * @author alex  
 * @version   
 * @since JDK 1.7  
 */
@Repository
public class LoctypeDaoImpl extends GenericMyIbatisDao<Loctype, Integer>implements LoctypeDao {
    /**
     * 命名空间
     */
    private static final String NAMESPACE = "com.zizaike.recommend.dao.LoctypeMapper." ;
    @Override
    public List<Loctype> queryLoctype(Loctype loctype) throws ZZKServiceException {
        return this.getSqlSession().selectList(NAMESPACE+"queryLoctype",loctype);
    }

}
  
