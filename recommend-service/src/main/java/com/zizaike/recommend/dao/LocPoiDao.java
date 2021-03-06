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

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.core.framework.springext.database.Slave;
import com.zizaike.entity.recommend.LocPoi;

/**
 * 
 * ClassName: LocPoiDao <br/>  
 * Function: poi. <br/>  
 * date: 2016年4月18日 下午3:46:39 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
public interface LocPoiDao {
   /**
    * 
    * queryLoctPoi:景点查询. <br/>  
    *  
    * @author snow.zhang  
    * @param poiName
    * @return
    * @throws ZZKServiceException  
    * @since JDK 1.7
    */
    @Slave
    List<LocPoi> queryLocPoi (String poiName)throws ZZKServiceException;
}
  
