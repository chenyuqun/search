/**  
 * Project Name:redis-service  <br/>
 * File Name:redisDao.java  <br/>
 * Package Name:com.zizaike.redis.dao  <br/>
 * Date:2015年11月9日下午4:08:39  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
 */

package com.zizaike.redis.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.JedisCluster;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zizaike.core.framework.cache.CacheKeyGenerater;
import com.zizaike.core.framework.cache.CacheKeyPrefix;
import com.zizaike.core.framework.cache.support.redis.RedisCacheDao;
import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import redis.clients.jedis.Tuple;

/**
 * ClassName:redisDao <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2015年11月9日 下午4:08:39 <br/>
 * 
 * @author snow.zhang
 * @version
 * @param <V>
 * @param <K>
 * @since JDK 1.7
 * @see
 */
@Repository
public class RedisCacheDaoImpl implements RedisCacheDao {
    @Autowired
    JedisCluster jedisCluster;

    @Override
    public <T> T get(final CacheKeyPrefix prefix, final String key, final Class<T> clazz) throws ZZKServiceException {
        String str = jedisCluster.get(CacheKeyGenerater.generateCacheKey(prefix, key));
        if (StringUtils.isNotBlank(str)) {
            return JSON.parseObject(str, clazz);
        }
        return null;
    }

    @Override
    public <T> T get(CacheKeyPrefix prefix, String key, TypeReference<T> reference) throws ZZKServiceException {
        String str = jedisCluster.get(CacheKeyGenerater.generateCacheKey(prefix, key));
        if (StringUtils.isNotBlank(str)) {
            return JSON.parseObject(str, reference);
        }
        return null;
    }

    @Override
    public <T> List<T> gets(CacheKeyPrefix prefix, Collection<String> keys, Class<T> clazz) throws ZZKServiceException {
        List<T> list = new ArrayList<T>();
        for (String key : keys) {
            list.add(get(prefix, key, clazz));
        }
        return list;
    }

    @Override
    public void set(CacheKeyPrefix prefix, String key, Object value) throws ZZKServiceException {
        if (value == null) {
            throw new IllegalParamterException("value could not bu null");
        }
        jedisCluster.set(CacheKeyGenerater.generateCacheKey(prefix, key), JSON.toJSONString(value));
    }

    @Override
    public void setEx(final CacheKeyPrefix prefix, String key, final Object value, final int seconds)
            throws ZZKServiceException {
        if (value == null) {
            throw new IllegalParamterException("value could not bu null");
        }
        jedisCluster.setex(CacheKeyGenerater.generateCacheKey(prefix, key), seconds, JSON.toJSONString(value));
    }

    @Override
    public Long delete(CacheKeyPrefix prefix, String key) {
        return jedisCluster.del(CacheKeyGenerater.generateCacheKey(prefix, key));
    }

    @Override
    public Long getKeyExpiresIn(CacheKeyPrefix prefix, String key) {
        return jedisCluster.ttl(CacheKeyGenerater.generateCacheKey(prefix, key));
    }

    @Override
    public boolean exist(CacheKeyPrefix prefix, String key) {
        return jedisCluster.exists(CacheKeyGenerater.generateCacheKey(prefix, key));
    }
    @Override
    public void zincrby (CacheKeyPrefix prefix, String key, double score, String member){
         jedisCluster.zincrby(CacheKeyGenerater.generateCacheKey(prefix, key),score,member);
    }
    @Override
    public Set<Tuple> zrevrangeWithScores (CacheKeyPrefix prefix, String key, long start, long end){
        return jedisCluster.zrevrangeWithScores(CacheKeyGenerater.generateCacheKey(prefix, key),start,end);
    }
}
