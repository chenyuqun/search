package com.zizaike.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zizaike.core.framework.cache.support.redis.RedisCacheDao;
import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.base.ChannelType;
import com.zizaike.entity.recommend.BNBServiceSearchStatistics;
import com.zizaike.is.redis.BNBServiceStatisticsRedisService;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKey;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKeyPrefix;

/**
 * 
 * ClassName: SearchRedisServiceImpl <br/>
 * Function: 服务查询统计服务. <br/>
 * date: 2015年12月8日 下午8:35:38 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.7
 */
@Service
public class BNBServiceStatisticsRedisServiceImpl implements BNBServiceStatisticsRedisService {
    private static final Double INCR = 1.0;

    @Autowired
    private RedisCacheDao redisCacheDao;

    @Override
    public void zincrBNBServiceSearch(BNBServiceSearchStatistics bnbServiceSearchStatistics) throws ZZKServiceException {
        if (bnbServiceSearchStatistics == null) {
            throw new IllegalParamterException("bnbServiceSearchStatistics is null");
        }
        ChannelType channelType = bnbServiceSearchStatistics.getChannel();
        if (channelType == null) {
            throw new IllegalParamterException("channelType is null");
        }
        if (bnbServiceSearchStatistics.getBnbServiceType() == null) {
            throw new IllegalParamterException("bnbServiceType is null");
        }
        if (bnbServiceSearchStatistics.getDestId() == null) {
            throw new IllegalParamterException("destId is null");
        }
        String member = JSON.toJSONString(bnbServiceSearchStatistics);
        redisCacheDao.zincrby(channelType == ChannelType.APP ? SearchRedisCacheKeyPrefix.APP_BNB_SERVICE
                : SearchRedisCacheKeyPrefix.WEB_BNB_SERVICE, SearchRedisCacheKey.FOREVER.getKey(), INCR,
                member);
    }

}
