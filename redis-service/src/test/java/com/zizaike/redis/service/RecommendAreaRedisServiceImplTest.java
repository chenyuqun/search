package com.zizaike.redis.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.vo.RecommendArea;
import com.zizaike.is.redis.RecommendAreaRedisService;
import com.zizaike.redis.basetest.BaseTest;

/**
 * 
 * ClassName: HotRecommendRedisServiceImplTest <br/>  
 * Function: 热点+行政<br/>  
 * date: 2015年11月10日 下午4:32:59 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
public class RecommendAreaRedisServiceImplTest extends BaseTest {
    @Autowired
    private RecommendAreaRedisService recommendAreaRedisService;
    @Test(description = "热点+行政")
    public void get() throws ZZKServiceException {
        RecommendArea recommendArea = recommendAreaRedisService.query();
        Assert.assertNotNull(recommendArea);;
    }
}
