package com.zizaike.redis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zizaike.core.framework.cache.support.redis.RedisCacheDao;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.hot.Recommend;
import com.zizaike.redis.basetest.BaseTest;
import com.zizaike.redis.constents.Prefix.SearchRedisCacheKeyPrefix;

/**
 * ClassName:RedisService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2015年11月9日 下午4:26:02 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.7
 * @see
 */
public class RedisServiceTest extends BaseTest {
    @Autowired
    private RedisCacheDao dao;
    
    @Test(description = "插入 倒计时")
    public void setEx() throws ZZKServiceException {
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
        System.err.println(JSON.toJSONString(list));
        dao.setEx(SearchRedisCacheKeyPrefix.RECOMMEND, "hot", list, 60*60);
    }
    @Test(description = "插入")
    public void set() throws ZZKServiceException {
        Recommend recommend = new Recommend();
        recommend.setRecommendName("测试setRecommendName");
        recommend.seteName("测试name");
        Recommend recommend2 = new Recommend();
        recommend2.setRecommendName("测试setRecommendName2");
        recommend2.seteName("测试name2");
        dao.set(SearchRedisCacheKeyPrefix.RECOMMEND, "hot1", recommend);
        dao.set(SearchRedisCacheKeyPrefix.RECOMMEND, "hot2", recommend2);
    }
    @Test(description = "查询引用对象")
    public void get() throws ZZKServiceException {
        List<Recommend> list = (List<Recommend>) dao.get(SearchRedisCacheKeyPrefix.RECOMMEND, "hot", new TypeReference<ArrayList<Recommend>>() {
        });
        Assert.assertNotEquals(list.size(), 2);
    }
    @Test(description = "查询对象")
    public void getObject() throws ZZKServiceException {
        Recommend str = dao.get(SearchRedisCacheKeyPrefix.RECOMMEND, "hot1",Recommend.class);
        Assert.assertEquals(str.getRecommendName(), "测试setRecommendName");
    }
    @Test(description = "批量查询对象")
    public void gets() throws ZZKServiceException {
        List<String> list = new ArrayList<String>();
        list.add("hot1");
        list.add("hot2");
        List<Recommend> str = dao.gets(SearchRedisCacheKeyPrefix.RECOMMEND, list,Recommend.class);
        Assert.assertEquals(str.size(), 2);
    }
    @Test(description = "得到倒计时")
    public void getKeyExpiresIn() throws ZZKServiceException {
        Long s = dao.getKeyExpiresIn(SearchRedisCacheKeyPrefix.RECOMMEND, "hot");
        Assert.assertNotNull(s);
    }
    @Test(description = "存在")
    public void exist() throws ZZKServiceException {
        Boolean s = dao.exist(SearchRedisCacheKeyPrefix.RECOMMEND, "hot1");
        System.err.println(s);
    }
    @Test(description = "删除")
    public void del() throws ZZKServiceException {
         dao.delete(SearchRedisCacheKeyPrefix.RECOMMEND, "hot1");
         dao.delete(SearchRedisCacheKeyPrefix.RECOMMEND, "hot");
         dao.delete(SearchRedisCacheKeyPrefix.RECOMMEND, "hot2");
    }
    
}
