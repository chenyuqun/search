package com.zizaike.recommend.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.vo.RecommendArea;
import com.zizaike.is.recommend.RecommendAreaService;
import com.zizaike.recommend.basetest.BaseTest;


/**
 * ClassName:TtopsAdaptTest <br/>
 * Date: 2015年3月5日 下午3:51:51 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.6
 * @see
 */
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class RecommendAreaServiceTest extends BaseTest {
  @Autowired
  private RecommendAreaService recommendAreaService;

  @Test(description = "查询热推+行政级别")
  public void quryRecommendArea() throws ZZKServiceException {
      RecommendArea recommendArea = recommendAreaService.query();
     Assert.assertNotNull(recommendArea);
  }

}
