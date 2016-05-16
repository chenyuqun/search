package com.zizaike.redis.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.util.JedisClusterCRC16;

import com.zizaike.core.framework.cache.support.redis.RedisCacheDao;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.redis.basetest.BaseTest;

/**
 * 
 * ClassName: RedisClusterTest <br/>  
 * Function: RedisCluster测试. <br/>  
 * date: 2016年5月15日 下午8:44:01 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
public class RedisClusterTest extends BaseTest {
    @Autowired
    private RedisCacheDao dao;
    
    @Test(description = "test")
    public void test() throws ZZKServiceException {
        String key = "2";
        // 这东西 可以直接看到key 的分片数，就能知道放哪个 节点
        System.out.println(JedisClusterCRC16.getSlot(key));
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.8.18", 7000));
        jedisClusterNodes.add(new HostAndPort("192.168.8.18", 7001));
        jedisClusterNodes.add(new HostAndPort("192.168.8.18", 7002));
        // 3个master 节点
        JedisCluster jc = new JedisCluster(jedisClusterNodes);
        System.out.println(jc.get(key));
        jc.setnx(key, "bar");
        String value = jc.get(key);
        System.out.println(value);
    }
    @Test(description = "test1")
    public void test1() throws ZZKServiceException {
        String key = "2";
        // 这东西 可以直接看到key 的分片数，就能知道放哪个 节点
        System.out.println(JedisClusterCRC16.getSlot(key));
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.8.18", 7003));
        jedisClusterNodes.add(new HostAndPort("192.168.8.18", 7004));
        jedisClusterNodes.add(new HostAndPort("192.168.8.18", 7005));
        // 3个master 节点
        JedisCluster jc = new JedisCluster(jedisClusterNodes);
        System.out.println(jc.get(key));
        jc.setnx(key, "bar");
        String value = jc.get(key);
        System.out.println(value);
    }
    @Test(description = "test2")
    public void test2() throws ZZKServiceException {
        String key = "2";
        // 这东西 可以直接看到key 的分片数，就能知道放哪个 节点
        System.out.println(JedisClusterCRC16.getSlot(key));
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.8.18", 7006));
        jedisClusterNodes.add(new HostAndPort("192.168.8.18", 7007));
        jedisClusterNodes.add(new HostAndPort("192.168.8.18", 7008));
        // 3个master 节点
        JedisCluster jc = new JedisCluster(jedisClusterNodes);
        System.out.println(jc.get(key));
        jc.setnx(key, "bar");
        String value = jc.get(key);
        System.out.println(value);
    }
    
}
