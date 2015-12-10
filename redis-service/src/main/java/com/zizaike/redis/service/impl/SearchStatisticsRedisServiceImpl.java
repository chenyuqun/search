package com.zizaike.redis.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Tuple;

import com.alibaba.fastjson.JSON;
import com.zizaike.core.framework.cache.support.redis.RedisCacheDao;
import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.base.ChannelType;
import com.zizaike.entity.recommend.SearchStatistics;
import com.zizaike.entity.recommend.SearchType;
import com.zizaike.entity.recommend.StatisticsType;
import com.zizaike.is.redis.SearchStatisticsRedisService;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKey;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKeyPrefix;
/**
 * 
 * ClassName: SearchRedisServiceImpl <br/>  
 * Function: 搜索统计服务. <br/>  
 * date: 2015年12月8日 下午8:35:38 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
@Service
public class SearchStatisticsRedisServiceImpl implements SearchStatisticsRedisService {
    private static final Long START = 0L;
    private static final Long END = 20L;
    private static final Long END_EVERY_DAY = 50L;
    private static final Double INCR = 1.0;

    @Autowired
    private RedisCacheDao redisCacheDao;

    @Override
    public void zincrHotSearch(SearchStatistics searchStatistics) throws ZZKServiceException {
        if(searchStatistics ==null){
            throw new IllegalParamterException("searchStatistics is null");
        }
        ChannelType channelType = searchStatistics.getChannel();
        if (channelType == null) {
            throw new IllegalParamterException("channelType is null");
        }
        String member = JSON.toJSONString(searchStatistics);
        redisCacheDao.zincrby(channelType == ChannelType.APP ? SearchRedisCacheKeyPrefix.APP_HOT : SearchRedisCacheKeyPrefix.WEB_HOT, SearchRedisCacheKey.FOREVER.getKey(), INCR, member);
    }


    @Override
    public List<SearchStatistics>  getHotSearch(ChannelType channelType) throws ZZKServiceException {
        if (channelType == null) {
            throw new IllegalParamterException("channelType is null");
        }
        Set<Tuple> set = redisCacheDao.zrevrangeWithScores(channelType == ChannelType.APP ? SearchRedisCacheKeyPrefix.APP_HOT : SearchRedisCacheKeyPrefix.WEB_HOT, SearchRedisCacheKey.FOREVER.getKey(), START, END);
        return getSearchStatisticsList(set,SearchType.HOT,StatisticsType.FOREVER,channelType);
    }

    @Override
    public void zincrHotSearchEveryDay(SearchStatistics searchStatistics) throws ZZKServiceException {
        if(searchStatistics ==null){
            throw new IllegalParamterException("searchStatistics is null");
        }
        ChannelType channelType = searchStatistics.getChannel();
        if (channelType == null) {
            throw new IllegalParamterException("channelType is null");
        }
        String member = JSON.toJSONString(searchStatistics);
        redisCacheDao.zincrby(channelType == ChannelType.APP ? SearchRedisCacheKeyPrefix.APP_HOT : SearchRedisCacheKeyPrefix.WEB_HOT, SearchRedisCacheKey.ERVERY_DAY.getKey(), INCR, member);
    }

    @Override
    public List<SearchStatistics>  getHotSearchEveryDay(ChannelType channelType) throws ZZKServiceException {
        if (channelType == null) {
            throw new IllegalParamterException("channelType is null");
        }
        Set<Tuple> set = redisCacheDao.zrevrangeWithScores(channelType == ChannelType.APP ? SearchRedisCacheKeyPrefix.APP_HOT : SearchRedisCacheKeyPrefix.WEB_HOT, SearchRedisCacheKey.ERVERY_DAY.getKey(), START, END_EVERY_DAY);
        return getSearchStatisticsList(set,SearchType.HOT,StatisticsType.ERVERY_DAY,channelType);
    }


    @Override
    public void zincrResultLessSearch( SearchStatistics searchStatistics) throws ZZKServiceException {
          
        if(searchStatistics ==null){
            throw new IllegalParamterException("searchStatistics is null");
        }
        ChannelType channelType = searchStatistics.getChannel();
        if (channelType == null) {
            throw new IllegalParamterException("channelType is null");
        }
        String member = JSON.toJSONString(searchStatistics);
        redisCacheDao.zincrby(channelType == ChannelType.APP ? SearchRedisCacheKeyPrefix.APP_RESULT_LESS : SearchRedisCacheKeyPrefix.WEB_RESULT_LESS, SearchRedisCacheKey.FOREVER.getKey(), INCR, member);
        
    }


    @Override
    public List<SearchStatistics>  getResultLessSearch(ChannelType channelType) throws ZZKServiceException {
          
        if (channelType == null) {
            throw new IllegalParamterException("channelType is null");
        }
        Set<Tuple> set = redisCacheDao.zrevrangeWithScores(channelType == ChannelType.APP ? SearchRedisCacheKeyPrefix.APP_RESULT_LESS : SearchRedisCacheKeyPrefix.WEB_RESULT_LESS, SearchRedisCacheKey.FOREVER.getKey(), START, END);
        return getSearchStatisticsList(set,SearchType.RESULT_LESS,StatisticsType.FOREVER,channelType);
    }


    @Override
    public void zincrResultLessSearchEveryDay(SearchStatistics searchStatistics) throws ZZKServiceException {
          
        if(searchStatistics ==null){
            throw new IllegalParamterException("searchStatistics is null");
        }
        ChannelType channelType = searchStatistics.getChannel();
        if (channelType == null) {
            throw new IllegalParamterException("channelType is null");
        }
        String member = JSON.toJSONString(searchStatistics);
        redisCacheDao.zincrby(channelType == ChannelType.APP ? SearchRedisCacheKeyPrefix.APP_RESULT_LESS : SearchRedisCacheKeyPrefix.WEB_RESULT_LESS, SearchRedisCacheKey.ERVERY_DAY.getKey(), INCR, member);
        
    }


    @Override
    public List<SearchStatistics>  getResultLessSearchEveryDay(ChannelType channelType) throws ZZKServiceException {
          
        if (channelType == null) {
            throw new IllegalParamterException("channelType is null");
        }
        Set<Tuple> set = redisCacheDao.zrevrangeWithScores(channelType == ChannelType.APP ? SearchRedisCacheKeyPrefix.APP_RESULT_LESS : SearchRedisCacheKeyPrefix.WEB_RESULT_LESS, SearchRedisCacheKey.ERVERY_DAY.getKey(), START, END_EVERY_DAY);
        return getSearchStatisticsList(set,SearchType.RESULT_LESS,StatisticsType.ERVERY_DAY,channelType);
    }
   /**
    * 
    * getSearchStatisticsSet:得到查询. <br/>  
    *  
    * @author snow.zhang  
    * @param tupleSet
    * @param searchType
    * @param statisticsType
    * @return  
    * @since JDK 1.7
    */
   private  List<SearchStatistics>  getSearchStatisticsList(Set<Tuple> tupleSet,SearchType searchType , StatisticsType statisticsType,ChannelType channelType){
       Iterator<Tuple> it = tupleSet.iterator();
       SearchStatistics searchStatistics = null;
       List<SearchStatistics>  list = new ArrayList<SearchStatistics>();
       while(it.hasNext()){
           Tuple tuple = it.next();
           searchStatistics = JSON.parseObject(tuple.getElement(), SearchStatistics.class);
           searchStatistics.setCount((long) tuple.getScore());
           searchStatistics.setSearchType(searchType);
           searchStatistics.setStatisticsType(statisticsType);
           searchStatistics.setChannel(channelType);
           list.add(searchStatistics);
       }
        return list;
    }
}
