/**  
 * Project Name:recommend-service  <br/>
 * File Name:LoctypeServiceTest.java  <br/>
 * Package Name:com.zizaike.recommend.service  <br/>
 * Date:2015年11月10日下午2:43:31  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.service;  

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.hot.Loctype;
import com.zizaike.is.recommend.LoctypeService;
import com.zizaike.recommend.basetest.BaseTest;

/**  
 * ClassName: LoctypeServiceTest <br/>  
 * Function: 地址测试. <br/>  
 * date: 2015年11月10日 下午2:43:31 <br/>  
 *  
 * @author alex  
 * @version   
 * @since JDK 1.7  
 */
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class LoctypeServiceTest extends BaseTest {
  @Autowired
  private LoctypeService loctypeService;

  @Test(description = "模糊查询地名")
  public void queryLoctype() throws ZZKServiceException {
      Loctype loc=new Loctype();
      loc.setDestId(10);
      loc.setTypeName("台");
      List<Loctype> list = loctypeService.queryLoctype(loc);
     Assert.assertNotEquals(0, list.size());
  }

}
  
