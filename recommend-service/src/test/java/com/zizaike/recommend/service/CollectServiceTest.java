/**  
 * Project Name:recommend-service  <br/>
 * File Name:LoctypeServiceTest.java  <br/>
 * Package Name:com.zizaike.recommend.service  <br/>
 * Date:2015年11月10日下午2:43:31  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.service;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.is.recommend.CollectService;
import com.zizaike.recommend.basetest.BaseTest;

/**
 * 
 * ClassName: CollectServiceTest <br/>  
 * Function: 收藏. <br/>  
 * date: 2016年4月25日 下午3:19:52 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class CollectServiceTest extends BaseTest {
  @Autowired
  private CollectService collectService;

  @Test(expectedExceptions=IllegalParamterException.class)
  public void bnbCollectionUserIdEmpty() throws ZZKServiceException {
      collectService.bnbCollection(null);
  }
  @Test(description = "民宿收藏")
  public void bnbCollection() throws ZZKServiceException {
     Assert.assertNotNull(collectService.bnbCollection(6499));
  }

}
  
