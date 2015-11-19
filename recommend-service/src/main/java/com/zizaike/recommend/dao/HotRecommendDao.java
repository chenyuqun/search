/**  
 * Project Name:recommend-service  <br/>
 * File Name:IRecommendDao.java  <br/>
 * Package Name:com.zizaike.recommend.dao  <br/>
 * Date:2015年11月4日下午2:48:57  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.dao;  


import java.util.List;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.core.framework.springext.database.Slave;
import com.zizaike.entity.recommend.Recommend;

/**  
 * ClassName:IRecommendDao <br/>  
 * Function: 推荐dao. <br/>  
 * Reason:  推荐. <br/>  
 * Date:     2015年11月4日 下午2:48:57 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public interface HotRecommendDao {
    /**
     * 
     * quryHotRecommend:热门推荐按国家代码查询. <br/>  
     *  
     * @author snow.zhang  
     * @param destId
     * @return
     * @throws ZZKServiceException  
     * @since JDK 1.7
     */
    @Slave
    List<Recommend> quryHotRecommend ()throws ZZKServiceException;
}
  
