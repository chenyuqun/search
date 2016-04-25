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

import com.zizaike.core.framework.springext.database.Slave;

/**
 * 
 * ClassName: CollectDao <br/>  
 * Function: 收藏. <br/>  
 * date: 2016年4月25日 下午2:44:47 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
public interface CollectDao {
    /**
     * 
     * bnbCollection: 用户收藏的民宿  hid以,分割. <br/>  
     *  
     * @author snow.zhang  
     * @param userId
     * @return  
     * @since JDK 1.7
     */
    @Slave
    String bnbCollection(Integer userId);
}
  
