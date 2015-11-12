package com.zizaike.redis.constents.prefix;

import com.zizaike.core.framework.cache.CacheKeyPrefix;

/**
 * 
 * ClassName: RedisCacheKeyPrefix <br/>
 * Function: 前缀:(域:业务) <br/>
 * date: 2015年11月10日 下午2:19:02 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.7
 */
public enum SearchRedisCacheKeyPrefix implements CacheKeyPrefix {
    /**
     * 查询域:推荐业务
     */
    RECOMMEND("search:recommend"), ;

    private String prefix;

    private SearchRedisCacheKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

}