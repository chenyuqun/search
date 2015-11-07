package com.zizaike.solr.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.core.framework.exception.ZZKServiceException;
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
}
