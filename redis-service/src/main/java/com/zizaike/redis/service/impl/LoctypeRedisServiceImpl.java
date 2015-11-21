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
import com.zizaike.entity.recommend.Loctype;
import com.zizaike.is.recommend.LoctypeService;
import com.zizaike.is.redis.LoctypeRedisService;
import com.zizaike.redis.constents.TimeType;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKey;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKeyPrefix;

/**
 * ClassName:HotRecommendRedisServiceImpl <br/>
 * Function: 行政数据缓存 <br/>
 * Date: 2015年11月10日 下午4:07:03 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.7
 * @see
 */
@Service
public class LoctypeRedisServiceImpl implements LoctypeRedisService {
    @Autowired
    private RedisCacheDao redisCacheDao;
    @Autowired
    private LoctypeService loctypeService;

    @Override
    public List<Loctype> queryByAreaLevel() throws ZZKServiceException {
        List<Loctype> list = null;
            list = redisCacheDao.get(SearchRedisCacheKeyPrefix.RECOMMEND, SearchRedisCacheKey.STATE_CITY.getKey(),
                    new TypeReference<ArrayList<Loctype>>() {
                    });
            if (list == null) {
                list = loctypeService.queryByAreaLevel();
                save(list);
            }
        return list;
    }

    @Override
    public void save(List<Loctype> list) throws ZZKServiceException {

        redisCacheDao.setEx(SearchRedisCacheKeyPrefix.RECOMMEND, SearchRedisCacheKey.STATE_CITY.getKey(), list,
                TimeType.DAY.getValue());

    }

}
