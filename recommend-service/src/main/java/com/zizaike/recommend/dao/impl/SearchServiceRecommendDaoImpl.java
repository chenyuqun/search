/**  
 * Project Name:recommend-service  <br/>
 * File Name:LoctypeDaoImpl.java  <br/>
 * Package Name:com.zizaike.recommend.dao.impl  <br/>
 * Date:2015年11月10日下午2:03:14  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
 */

package com.zizaike.recommend.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zizaike.core.common.page.Page;
import com.zizaike.core.common.page.PageList;
import com.zizaike.core.framework.mybatis.impl.GenericMyIbatisDao;
import com.zizaike.entity.recommend.SearchServiceRecommend;
import com.zizaike.recommend.dao.SearchServiceRecommendDao;

@Repository
public class SearchServiceRecommendDaoImpl extends GenericMyIbatisDao<SearchServiceRecommend, Integer> implements
        SearchServiceRecommendDao {
    /**
     * 命名空间
     */
    private static final String NAMESPACE = "com.zizaike.recommend.dao.SearchServiceRecommendMapper.";

    @Override
    public PageList<SearchServiceRecommend> query(Page page,Map<String, Object> paramMap) {
        PageList<SearchServiceRecommend> pageList = new PageList<SearchServiceRecommend>();
        int totalCount = count();
        page.setTotalCount(totalCount);
        if(paramMap==null){
            paramMap = new HashMap<String, Object>();
        }
        paramMap.put("startIndex", Integer.valueOf(page.getStartIndex()));//设置起始
        paramMap.put("pageSize", Integer.valueOf(page.getPageSize()));//设置结束
       List<SearchServiceRecommend> list =   this.getSqlSession().selectList(NAMESPACE + "queryServiceRecommend",paramMap);
       pageList.setList(list);
       pageList.setPage(page);
         return pageList;
    }

    public Integer count() {
        return this.getSqlSession().selectOne(NAMESPACE + "queryServiceRecommendCount");
    }

    @Override
    public List<SearchServiceRecommend> queryAll() {
        return  this.getSqlSession().selectList(NAMESPACE + "queryAll");
    }

}
