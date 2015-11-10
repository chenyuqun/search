/**  
 * Project Name:recommend-service  <br/>
 * File Name:HotRecommendServiceImpl.java  <br/>
 * Package Name:com.zizaike.recommend.service.impl  <br/>
 * Date:2015年11月5日下午3:22:05  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.service.impl;  

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.hot.Recommend;
import com.zizaike.is.recommend.HotRecommendService;
import com.zizaike.is.redis.HotRecommendRedisService;
import com.zizaike.recommend.dao.HotRecommendDao;

/**  
 * ClassName:HotRecommendServiceImpl <br/>  
 * Function: 热门推荐impl. <br/>  
 * Reason:   service. <br/>
 * Date:     2015年11月5日 下午3:22:05 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
@Service
public class HotRecommendServiceImpl implements HotRecommendService {
    @Autowired
   private HotRecommendDao hotRecommendDao;
    @Autowired
   private HotRecommendRedisService HotRecommendRedisService;

    @Override
    public List<Recommend> quryHotRecommend() throws ZZKServiceException {
        List<Recommend> list = HotRecommendRedisService.qury();
        if(list==null){
            list = hotRecommendDao.quryHotRecommend();
            //保存缓存
            HotRecommendRedisService.save(list);
        }
        return list;
    }

}
  
