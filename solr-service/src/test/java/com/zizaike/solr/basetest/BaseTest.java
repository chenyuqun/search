package com.zizaike.solr.basetest;


import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = {"classpath:/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class BaseTest extends AbstractTransactionalTestNGSpringContextTests {
    
}
