/**  
 * Project Name:recommend-service  <br/>
 * File Name:LoctypeDao.java  <br/>
 * Package Name:com.zizaike.recommend.dao  <br/>
 * Date:2015年11月10日下午2:00:39  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
/**  
 * Project Name:recommend-service <br/> 
 * File Name:LoctypeDao.java  <br/>
 * Package Name:com.zizaike.recommend.dao  <br/>
 * Date:2015年11月10日下午2:00:39  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved. 
 *  
 */  
  
package com.zizaike.recommend.dao;  

import java.util.List;
import java.util.Map;

import com.zizaike.core.common.page.Page;
import com.zizaike.core.common.page.PageList;
import com.zizaike.core.framework.springext.database.Slave;
import com.zizaike.entity.recommend.SearchServiceRecommend;

/**
 * 
 * ClassName: CollectDao <br/>  
 * Function: 推荐服务. <br/>  
 * date: 2016年4月25日 下午2:44:47 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
public interface SearchServiceRecommendDao {
    /**
     * 
     * query:查询推荐服务. <br/>  
     *  
     * @author snow.zhang  
     * @return  
     * @since JDK 1.7
     */
    @Slave
    PageList<SearchServiceRecommend> query(Page page,Map<String, Object> paramMap);
    /**
     * 
     * queryAll:查找所有. <br/>  
     *  
     * @author snow.zhang  
     * @return  
     * @since JDK 1.7
     */
    @Slave
    List<SearchServiceRecommend> queryAll();
}
  
