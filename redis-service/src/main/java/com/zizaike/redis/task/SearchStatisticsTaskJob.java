package com.zizaike.redis.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zizaike.core.framework.cache.support.redis.RedisCacheDao;
import com.zizaike.entity.base.ChannelType;
import com.zizaike.is.recommend.SearchStatisticsService;
import com.zizaike.is.redis.SearchStatisticsRedisService;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKey;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKeyPrefix;
/**
 * 
 * ClassName: SearchStatisticsTaskJob <br/>  
 * Function: 搜索统计任务. <br/>  
 * date: 2015年12月10日 上午9:54:20 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
@Component  
public class SearchStatisticsTaskJob { 
    protected final Logger LOG = LoggerFactory.getLogger(SearchStatisticsTaskJob.class);
    @Autowired
    private SearchStatisticsService searchStatisticsService;
    @Autowired
    private SearchStatisticsRedisService searchStatisticsRedisService;
    @Autowired
    private RedisCacheDao redisCacheDao;
    //每天23点同步数据
   private static final String TIME = "0 30 23 * * ?";
    /**
     * 
     * HotSearchStatistics:热推统计. <br/>  
     *  
     * @author snow.zhang    
     * @since JDK 1.7
     */
    @Scheduled(cron = TIME)  
    public void HotSearchStatistics() { 
        long start = System.currentTimeMillis();
        try {
            searchStatisticsService.addBatch(searchStatisticsRedisService.getHotSearch(ChannelType.APP));
            searchStatisticsService.addBatch(searchStatisticsRedisService.getHotSearch(ChannelType.WEB));
        } catch (Exception e) {
            e.printStackTrace();  
            LOG.error("when call HotSearchStatistics, error{}",e.toString());
        }finally{
            LOG.info("when call HotSearchStatistics, use: {}ms", System.currentTimeMillis() - start);
        }
    }  
    /**
     * 
     * HotSearchStatisticsEveryDay:热推荐每天. <br/>  
     * @author snow.zhang    
     * @since JDK 1.7
     */
    @Scheduled(cron = TIME)  
    public void HotSearchStatisticsEveryDayApp() { 
        long start = System.currentTimeMillis();
        try {
            searchStatisticsService.addBatch(searchStatisticsRedisService.getHotSearchEveryDay(ChannelType.APP));
            redisCacheDao.delete(SearchRedisCacheKeyPrefix.APP_HOT, SearchRedisCacheKey.ERVERY_DAY.getKey());
        } catch (Exception e) {
            e.printStackTrace();  
            LOG.error("when call HotSearchStatisticsEveryDayApp, error{}",e.toString());
        }finally{
            LOG.info("when call HotSearchStatisticsEveryDayApp, use: {}ms", System.currentTimeMillis() - start);
        }
    }  
    @Scheduled(cron = TIME)  
    public void HotSearchStatisticsEveryDayWeb() { 
        long start = System.currentTimeMillis();
        try {
            searchStatisticsService.addBatch(searchStatisticsRedisService.getHotSearchEveryDay(ChannelType.WEB));
            redisCacheDao.delete(SearchRedisCacheKeyPrefix.WEB_HOT, SearchRedisCacheKey.ERVERY_DAY.getKey());
        } catch (Exception e) {
            e.printStackTrace();  
            LOG.error("when call HotSearchStatisticsEveryDayWeb, error{}",e.toString());
        }finally{
            LOG.info("when call HotSearchStatisticsEveryDayWeb, use: {}ms", System.currentTimeMillis() - start);
        }
    }  
    @Scheduled(cron = TIME)  
    public void ResultLessSearch() { 
        long start = System.currentTimeMillis();
        try {
            searchStatisticsService.addBatch(searchStatisticsRedisService.getResultLessSearch(ChannelType.APP));
            searchStatisticsService.addBatch(searchStatisticsRedisService.getResultLessSearch(ChannelType.WEB));
        } catch (Exception e) {
            e.printStackTrace();  
            LOG.error("when call ResultLessSearch, error{}",e.toString());
        }finally{
            LOG.info("when call ResultLessSearch, use: {}ms", System.currentTimeMillis() - start);
        }
    }  
    @Scheduled(cron = TIME)  
    public void ResultLessSearchEveryDayWeb() { 
        long start = System.currentTimeMillis();
        try {
            searchStatisticsService.addBatch(searchStatisticsRedisService.getResultLessSearchEveryDay(ChannelType.WEB));
            redisCacheDao.delete(SearchRedisCacheKeyPrefix.WEB_RESULT_LESS, SearchRedisCacheKey.ERVERY_DAY.getKey());
        } catch (Exception e) {
            e.printStackTrace();  
            LOG.error("when call ResultLessSearchEveryDayWeb, error{}",e.toString());
        }finally{
            LOG.info("when call ResultLessSearchEveryDayWeb, use: {}ms", System.currentTimeMillis() - start);
        }
    }  
    @Scheduled(cron = TIME)  
    public void ResultLessSearchEveryDayAPP() { 
        long start = System.currentTimeMillis();
        try {
            searchStatisticsService.addBatch(searchStatisticsRedisService.getResultLessSearchEveryDay(ChannelType.APP));
            redisCacheDao.delete(SearchRedisCacheKeyPrefix.APP_RESULT_LESS, SearchRedisCacheKey.ERVERY_DAY.getKey());
        } catch (Exception e) {
            e.printStackTrace();  
            LOG.error("when call ResultLessSearchEveryDayAPP, error{}",e.toString());
        }finally{
            LOG.info("when call ResultLessSearchEveryDayAPP, use: {}ms", System.currentTimeMillis() - start);
        }
    }  
}  