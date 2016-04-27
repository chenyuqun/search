/**  
 * Project Name:redis-service  <br/>
 * File Name:HotRecommendRedisServiceImpl.java  <br/>
 * Package Name:com.zizaike.redis.service.impl  <br/>
 * Date:2015年11月10日下午4:07:03  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
 */

package com.zizaike.redis.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;
import com.zizaike.core.framework.cache.support.redis.RedisCacheDao;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.Recommend;
import com.zizaike.entity.solr.ServiceSearchVo;
import com.zizaike.is.recommend.HotRecommendService;
import com.zizaike.is.redis.HotRecommendRedisService;
import com.zizaike.is.solr.UserSolrService;
import com.zizaike.redis.constents.TimeType;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKey;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKeyPrefix;

/**
 * ClassName:HotRecommendRedisServiceImpl <br/>
 * Function: 用户热推缓存服务 <br/>
 * Date: 2015年11月10日 下午4:07:03 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.7
 * @see
 */
@Service
public class HotRecommendRedisServiceImpl implements HotRecommendRedisService {
    @Autowired
    private RedisCacheDao redisCacheDao;
    @Autowired
    private HotRecommendService hotRecommendService;
    @Autowired
    private UserSolrService userSolrService;

    @Override
    public List<Recommend> qury() throws ZZKServiceException {
        List<Recommend> list = null;
        list = redisCacheDao.get(SearchRedisCacheKeyPrefix.RECOMMEND, SearchRedisCacheKey.HOT.getKey(),
                new TypeReference<ArrayList<Recommend>>() {
                });
        if (list == null) {
            list = hotRecommendService.quryHotRecommend();
            save(list);
        }
        return list;
    }

    /**
     * 
     * 缓存一天.
     */
    @Override
    public void save(List<Recommend> value) throws ZZKServiceException {
        redisCacheDao.setEx(SearchRedisCacheKeyPrefix.RECOMMEND, SearchRedisCacheKey.HOT.getKey(), value,
                TimeType.DAY.getValue());
    }


}
