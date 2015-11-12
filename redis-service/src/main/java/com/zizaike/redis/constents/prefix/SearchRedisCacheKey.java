package com.zizaike.redis.constents.prefix;

import com.zizaike.core.framework.cache.CacheKey;

/**
 * 
 * ClassName: RedisCacheKeyPrefix <br/>
 * Function: key<br/>
 * date: 2015年11月10日 下午2:19:02 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.7
 */
public enum SearchRedisCacheKey implements CacheKey {
    /**
     * 热推荐业务
     */
    HOT("hot"), ;
    private String key;

    private SearchRedisCacheKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

}