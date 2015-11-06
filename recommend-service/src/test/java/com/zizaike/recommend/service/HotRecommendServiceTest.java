package com.zizaike.recommend.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.entity.recommend.hot.Recommend;
import com.zizaike.is.recommend.HotRecommendService;
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
public class HotRecommendServiceTest extends BaseTest {
  @Autowired
  private HotRecommendService hotRecommendService;

  @Test(description = "查询热推")
  public void quryHotRecommend() {
      List<Recommend> list = hotRecommendService.quryHotRecommend();
     Assert.assertNotEquals(0, list.size());
  }

}
