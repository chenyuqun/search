package com.zizaike.solr.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.entity.solr.User;
import com.zizaike.is.solr.UserSolrService;
import com.zizaike.solr.example.test.AbstractSolrIntegrationTest;

public  class UserTest extends AbstractSolrIntegrationTest {

  @Autowired
  UserSolrService userService;
  @Test
  public void testQueryUserById() {
     User user =  userService.queryUserById(8706);
     Assert.assertNotNull(user, "user is null");
  }
}
