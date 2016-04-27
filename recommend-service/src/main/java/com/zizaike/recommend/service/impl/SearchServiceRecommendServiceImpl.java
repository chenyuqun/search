/**  
 * Project Name:recommend-service  <br/>
 * File Name:LoctypeServiceImpl.java  <br/>
 * Package Name:com.zizaike.loctype.service.impl  <br/>
 * Date:2015年11月10日下午1:55:02  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.service.impl;  

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zizaike.core.common.page.Page;
import com.zizaike.core.common.page.PageList;
import com.zizaike.entity.recommend.SearchServiceRecommend;
import com.zizaike.is.recommend.SearchServiceRecommendService;
import com.zizaike.recommend.dao.SearchServiceRecommendDao;

/**
 * 
 * ClassName: SearchServiceRecommendServiceImpl <br/>  
 * Function: 查询服务. <br/>  
 * date: 2016年4月26日 下午5:47:50 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
@Service
public class SearchServiceRecommendServiceImpl implements SearchServiceRecommendService {
    @Autowired
   private SearchServiceRecommendDao searchServiceRecommendDao;
    @Override
    public PageList<SearchServiceRecommend> query(Page page,Map<String, Object> paramMap) {
        return searchServiceRecommendDao.query(page,paramMap);
    }
    @Override
    public List<SearchServiceRecommend> queryAll() {
          
        return searchServiceRecommendDao.queryAll();
    }
}
  
