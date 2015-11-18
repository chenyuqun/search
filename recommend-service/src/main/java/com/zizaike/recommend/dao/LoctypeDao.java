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
import com.zizaike.entity.recommend.hot.Loctype;

/**  
 * ClassName: LoctypeDao <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2015年11月10日 下午2:00:39 <br/>  
 *  
 * @author zzk  
 * @version   
 * @since JDK 1.7  
 */
public interface LoctypeDao {
    /**
     * 
     * quryLoctype:热门推荐按国家代码查询. <br/>  
     *  
     * @author alex 
     * @param destId
     * @return
     * @throws ZZKServiceException  
     * @since JDK 1.7
     */
    @Slave
    List<Loctype> queryLoctype (Loctype loctype)throws ZZKServiceException;
    /**
     * 
     * queryByAreaLevel:查询行政级别. <br/>  
     *  
     * @author snow.zhang  
     * @return
     * @throws ZZKServiceException  
     * @since JDK 1.7
     */
    @Slave
    List<Loctype> queryByAreaLevel ()throws ZZKServiceException;
}
  
