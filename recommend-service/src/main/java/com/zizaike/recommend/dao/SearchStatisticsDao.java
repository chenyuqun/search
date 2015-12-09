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
import com.zizaike.core.framework.springext.database.Master;
import com.zizaike.entity.recommend.SearchStatistics;

/**  
 * ClassName: LoctypeDao <br/>  
 * Function: top. <br/>  
 * date: 2015年11月10日 下午2:00:39 <br/>  
 *  
 * @author snow.zhang
 * @version   
 * @since JDK 1.7  
 */
public interface SearchStatisticsDao {
    /**
     * 
     * addBatch:批量导入. <br/>  
     *  
     * @author snow.zhang  
     * @param list
     * @throws ZZKServiceException  
     * @since JDK 1.7
     */
    @Master
     void addBatch (List<SearchStatistics> list)throws ZZKServiceException;
}
  
