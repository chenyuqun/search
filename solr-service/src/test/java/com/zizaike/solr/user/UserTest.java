package com.zizaike.solr.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.core.common.page.PageList;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.base.ChannelType;
import com.zizaike.entity.solr.BNBServiceType;
import com.zizaike.entity.solr.ServiceSearchVo;
import com.zizaike.entity.solr.User;
import com.zizaike.entity.solr.dto.AssociateWordsDTO;
import com.zizaike.is.solr.UserSolrService;
import com.zizaike.solr.example.test.AbstractSolrIntegrationTest;

public  class UserTest extends AbstractSolrIntegrationTest {

  @Autowired
  UserSolrService userService;
  @Test
  public void testQueryUserById() throws ZZKServiceException {
     User user =  userService.queryUserById(8706);
     Assert.assertNotNull(user, "user is null");
  }
  
  @Test
  public void testQueryUserByWordsAndLoc() throws ZZKServiceException {
     List<AssociateWordsDTO> user =  userService.queryUserByWordsAndLoc("淡水",10,60515);
     Assert.assertNotNull(user, "user is null");
  }
  @Test
  public void serviceQuery() throws ZZKServiceException {
      ServiceSearchVo serviceSearchVo = new ServiceSearchVo();
      serviceSearchVo.setChannel(ChannelType.APP);
      serviceSearchVo.setDestId(10);
      serviceSearchVo.setMultilang(1);
      serviceSearchVo.setPage(1);
      serviceSearchVo.setServiceType(BNBServiceType.BOOKING);
      PageList<com.zizaike.entity.solr.dto.User> list =  userService.serviceQuery(serviceSearchVo);
      System.err.println(list);
      Assert.assertNotEquals(list.getList().size(), 0);
  }
}
