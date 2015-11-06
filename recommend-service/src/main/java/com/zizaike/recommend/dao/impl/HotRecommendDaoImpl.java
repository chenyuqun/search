package com.zizaike.recommend.dao.impl;  

import java.util.List;

import com.zizaike.core.framework.exception.ServiceException;
import com.zizaike.core.framework.mybatis.impl.GenericMyIbatisDao;
import com.zizaike.entity.recommend.hot.Recommend;
import com.zizaike.recommend.dao.HotRecommendDao;

/**  
 * ClassName:HotRecommendDaoImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2015年11月4日 下午9:53:30 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public class HotRecommendDaoImpl extends GenericMyIbatisDao<Recommend, Integer>implements HotRecommendDao {
    /**
     * 命名空间
     */
    private static final String NAMESPACE = "com.zizaike.recommend.RecommendMapper." ;
    @Override
    public List<Recommend> quryHotRecommend() throws ServiceException {
        return this.getSqlSession().selectList(NAMESPACE+"quryHotRecommend");
    }

}
  
