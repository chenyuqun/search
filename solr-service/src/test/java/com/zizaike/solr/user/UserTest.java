package com.zizaike.solr.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.core.common.page.PageList;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.base.ChannelType;
import com.zizaike.entity.solr.BNBServiceType;
import com.zizaike.entity.solr.SearchType;
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
      serviceSearchVo.setDestId(12);
      serviceSearchVo.setMultilang(1);
      serviceSearchVo.setMultiprice(12);
      serviceSearchVo.setPage(2);
      serviceSearchVo.setServiceType(BNBServiceType.BUS_SERVICE);
//      serviceSearchVo.setSearchid(2905);
//      serviceSearchVo.setSearchType(SearchType.CITY);
      //垦丁
      serviceSearchVo.setSearchid(0);
      serviceSearchVo.setSearchType(SearchType.CITY);
      PageList<com.zizaike.entity.solr.dto.User> list =  userService.serviceQuery(serviceSearchVo);
      System.err.println(list.getList());
      System.err.println(list.getPage());
      Assert.assertNotEquals(list.getList().size(), 0);
  }
  @Test
  public void serviceRecommend() throws ZZKServiceException {
      ServiceSearchVo serviceSearchVo = new ServiceSearchVo();
      serviceSearchVo.setMultiprice(12);
      PageList<com.zizaike.entity.solr.dto.User> list =  userService.serviceRecommend(serviceSearchVo);
      Assert.assertNotEquals(list.getList().size(), 0);
  }
}
