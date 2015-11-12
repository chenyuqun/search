package com.zizaike.recommend.dao.impl;  

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.core.framework.mybatis.impl.GenericMyIbatisDao;
import com.zizaike.entity.recommend.hot.Recommend;
import com.zizaike.recommend.dao.HotRecommendDao;

/**  
 * ClassName:HotRecommendDaoImpl <br/>  
 * Function: 热门推荐dao. <br/>  
 * Reason:   推荐dao. <br/>  
 * Date:     2015年11月4日 下午9:53:30 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
@Repository
public class HotRecommendDaoImpl extends GenericMyIbatisDao<Recommend, Integer>implements HotRecommendDao {
    /**
     * 命名空间
     */
    private static final String NAMESPACE = "com.zizaike.recommend.dao.RecommendMapper." ;
    @Override
    public List<Recommend> quryHotRecommend() throws ZZKServiceException {
        return this.getSqlSession().selectList(NAMESPACE+"quryHotRecommend");
    }

}
  
