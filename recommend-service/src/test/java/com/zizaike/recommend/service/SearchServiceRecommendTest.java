/**  
 * Project Name:recommend-service  <br/>
 * File Name:LoctypeServiceTest.java  <br/>
 * Package Name:com.zizaike.recommend.service  <br/>
 * Date:2015年11月10日下午2:43:31  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.service;  

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.core.common.page.Page;
import com.zizaike.core.common.page.PageList;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.SearchServiceRecommend;
import com.zizaike.is.recommend.SearchServiceRecommendService;
import com.zizaike.recommend.basetest.BaseTest;

/**
 * 
 * ClassName: LocPoiServiceTest <br/>  
 * Function:  服务推荐. <br/>  
 * date: 2016年4月18日 下午4:09:50 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class SearchServiceRecommendTest extends BaseTest {
  @Autowired
  private SearchServiceRecommendService searchServiceRecommendService;

  @Test(description = "服务推荐查询")
  public void query() throws ZZKServiceException {
      PageList<SearchServiceRecommend> list = searchServiceRecommendService.query(new Page(),new HashMap<String, Object>());
     Assert.assertNotEquals(0, list.getList().size());
  }

}
  
