package com.zizaike.redis;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.zizaike.core.common.PropertyConfigurer;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * 
 * ClassName: JedisClusterFactory <br/>
 * Function: 集群factory. <br/>
 * date: 2015年11月10日 上午10:01:47 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.7
 */
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(JedisClusterFactory.class);
    @Autowired
    private PropertyConfigurer propertyConfigurer;
    private String addressKeyPrefix;
    private JedisCluster jedisCluster;
    private Integer timeout;
    private Integer maxRedirections;
    private GenericObjectPoolConfig genericObjectPoolConfig;

    private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

    @Override
    public JedisCluster getObject() throws Exception {
        return jedisCluster;
    }

    @Override
    public Class<? extends JedisCluster> getObjectType() {
        return this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
    /**
     * 
     * parseHostAndPort:解析port. <br/>  
     *  
     * @author snow.zhang  
     * @return
     * @throws Exception  
     * @since JDK 1.7
     */
    private Set<HostAndPort> parseHostAndPort() {
        try {
            Set<HostAndPort> haps = new HashSet<HostAndPort>();
            for (Object key : propertyConfigurer.getPropertyKeySet()) {
                if (!((String) key).startsWith(addressKeyPrefix)) {
                    continue;
                }
                String val = propertyConfigurer.getProperty((String)key);
                boolean isIpPort = p.matcher(val).matches();
                if (!isIpPort) {
                    throw new IllegalArgumentException("ip 或 port 不合法");
                }
                LOG.info(key+"="+val);
                String[] ipAndPort = val.split(":");

                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }

            return haps;
        } catch (IllegalArgumentException ex) {
            throw ex;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> haps = this.parseHostAndPort();

        jedisCluster = new JedisCluster(haps, timeout, maxRedirections, genericObjectPoolConfig);

    }
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public void setAddressKeyPrefix(String addressKeyPrefix) {
        this.addressKeyPrefix = addressKeyPrefix;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

}