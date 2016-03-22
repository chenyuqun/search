/**  
 * Project Name:redis-service  <br/>
 * File Name:HotRecommendRedisServiceImpl.java  <br/>
 * Package Name:com.zizaike.redis.service.impl  <br/>
 * Date:2015年11月10日下午4:07:03  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
 */

package com.zizaike.redis.service.impl;


import com.zizaike.entity.recommend.Loctype;
import com.zizaike.entity.recommend.vo.CountryArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zizaike.core.framework.cache.support.redis.RedisCacheDao;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.vo.RecommendArea;
import com.zizaike.is.recommend.RecommendAreaService;
import com.zizaike.is.redis.RecommendAreaRedisService;
import com.zizaike.redis.constents.TimeType;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKey;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKeyPrefix;

import java.util.ArrayList;
import java.util.List;

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
public class RecommendAreaRedisServiceImpl implements RecommendAreaRedisService {
    @Autowired
    private RedisCacheDao redisCacheDao;
    @Autowired
    private RecommendAreaService reService;

    @Override
    public RecommendArea query() throws ZZKServiceException {
        RecommendArea  recommendArea = new RecommendArea();
            recommendArea = redisCacheDao.get(SearchRedisCacheKeyPrefix.RECOMMEND, SearchRedisCacheKey.HOT_STATE_CITY.getKey(), RecommendArea.class);
        if(recommendArea == null){
            recommendArea = reService.query();
            save(recommendArea);
        }
        
        return recommendArea;
    }

    @Override
    public List<Loctype> queryByDest(Integer destId) throws ZZKServiceException {
        RecommendArea  recommendArea = new RecommendArea();
        recommendArea = redisCacheDao.get(SearchRedisCacheKeyPrefix.RECOMMEND, SearchRedisCacheKey.HOT_STATE_CITY.getKey(), RecommendArea.class);
        if(recommendArea == null){
            recommendArea = reService.query();
            save(recommendArea);
        }
        List<Loctype> loctypeList=new ArrayList<Loctype>();
        for(CountryArea countryArea:recommendArea.getCountryAreas()){
            if(destId==countryArea.getCountry().getDestId()){
                loctypeList=countryArea.getAreaList();
                break;
            }
        }
        return loctypeList;
    }

    @Override
    public void save(RecommendArea value) throws ZZKServiceException {
        redisCacheDao.setEx(SearchRedisCacheKeyPrefix.RECOMMEND, SearchRedisCacheKey.HOT_STATE_CITY.getKey(), value,
                TimeType.DAY.getValue());
    }

}
