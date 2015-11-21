package com.zizaike.redis.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.alibaba.fastjson.TypeReference;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.Recommend;
import com.zizaike.is.redis.HotRecommendRedisService;
import com.zizaike.redis.basetest.BaseTest;
import com.zizaike.redis.constents.prefix.SearchRedisCacheKeyPrefix;

/**
 * 
 * ClassName: HotRecommendRedisServiceImplTest <br/>  
 * Function: 测试 <br/>  
 * date: 2015年11月10日 下午4:32:59 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
public class HotRecommendRedisServiceImplTest extends BaseTest {
    @Autowired
    private HotRecommendRedisService hotRecommendRedisService;
    @Test(description = "查询热推对象")
    public void get() throws ZZKServiceException {
        List<Recommend> list = hotRecommendRedisService.qury();
        Assert.assertNotEquals(list.size(), 0);
    }
    @Test(description = "保存热推对象")
    public void save() throws ZZKServiceException {
        List<Recommend> list = new ArrayList<Recommend>();
        Recommend recommend = new Recommend();
        recommend.setRecommendName("测试");
        list.add(recommend);
        Recommend recommend1 = new Recommend();
        recommend1.setRecommendName("测试1");
        list.add(recommend1);
        Recommend recommend2 = new Recommend();
        recommend2.setRecommendName("测试2");
        list.add(recommend2);
       hotRecommendRedisService.save(list);
    }
}
