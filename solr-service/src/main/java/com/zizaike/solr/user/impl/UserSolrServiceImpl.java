/**  
 * Project Name:solr-service  <br/>
 * File Name:UserServiceImpl.java  <br/>
 * Package Name:com.zizaike.solr.user.impl  <br/>
 * Date:2015年10月28日下午5:01:35  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
 */

package com.zizaike.solr.user.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.zizaike.core.common.page.PageList;
import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.BNBServiceSearchStatistics;
import com.zizaike.entity.solr.BNBServiceType;
import com.zizaike.entity.solr.ServiceSearchVo;
import com.zizaike.entity.solr.User;
import com.zizaike.entity.solr.dto.AssociateType;
import com.zizaike.entity.solr.dto.AssociateWordsDTO;
import com.zizaike.entity.solr.model.SolrSearchableUserFields;
import com.zizaike.is.solr.UserSolrService;
import com.zizaike.solr.domain.event.BNBServiceSearchApplicationEvent;

/**
 * ClassName:UserServiceImpl <br/>
 * Function: 民宿查询实现. <br/>
 * Reason:民宿查询. <br/>
 * Date: 2015年10月28日 下午5:01:35 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.7
 * @see
 */
@NoRepositoryBean
public class UserSolrServiceImpl extends SimpleSolrRepository<User, Integer>  implements UserSolrService {
    private static final Logger LOG = LoggerFactory.getLogger(UserSolrServiceImpl.class);
    @Autowired
    ApplicationContext applicationContext;
    private static final Integer pageSize = 10;
    @Override
    public User queryUserById(Integer id) throws ZZKServiceException {
        long start = System.currentTimeMillis();
        if (id == null) {
            throw new IllegalParamterException("id is null");
        }
        User user = findOne(id);
        LOG.info("when call queryUserById, use: {}ms", System.currentTimeMillis() - start);
        return user;
    }
    
    @Override
    public List<AssociateWordsDTO> queryUserByWordsAndLoc(String words,Integer destId,Integer locid) throws ZZKServiceException {
        long start = System.currentTimeMillis();
        if (words == null) {
            throw new IllegalParamterException("words is null");
        }
        if (locid == null) {
            throw new IllegalParamterException("locid is null");
        }
        if (destId == null) {
            throw new IllegalParamterException("destId is null");
        }
        List<AssociateWordsDTO> associateWords=new ArrayList<AssociateWordsDTO>();
        //List<User> user=new ArrayList<User>();
        SimpleQuery query = new SimpleQuery(new Criteria(SolrSearchableUserFields.USERNAME).is(words));
        /*
         * 测试民宿 特例
         */   
        ArrayList<Integer> values=new ArrayList<Integer>();
        values.add(66);
        values.add(40080);
        values.add(40793);
        values.add(292734);  
        query.addCriteria(new Criteria(SolrSearchableUserFields.ID).is(values).not());
        //1为商圈
        if(locid!=0){
            query.addCriteria(new Criteria(SolrSearchableUserFields.LOCATION_TYPEID).is(locid));
        }
        //加上locId限制
        if(destId!=0){
            query.addCriteria(new Criteria(SolrSearchableUserFields.DEST_ID).is(destId));
        }
        //有效民宿
        query.addCriteria(new Criteria(SolrSearchableUserFields.STATUS).is(1));
        //认证
        query.addCriteria(new Criteria(SolrSearchableUserFields.VERIFIED_BY_ZZK).is(1));
        //最多2条记录
        query.setRows(10);
        Iterator<User> username=getSolrOperations().queryForPage(query, User.class).iterator();
        List<Integer> ids=new ArrayList<Integer>();
        while(username.hasNext()){
            //user.add(username.next());
            User user=username.next();
            AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
            if(user.getId()!=null){
            associateWordsDTO.setUid(user.getId());
            ids.add(user.getId());
            }
            associateWordsDTO.setAssociateType(AssociateType.HOMESTAY_NAME);
            if(user.getDestId()!=null){
            associateWordsDTO.setDestId(user.getDestId());
            }
            associateWordsDTO.setName(user.getUsername()==null?"":user.getUsername());
            associateWordsDTO.setAddress(user.getAddress()==null?"":user.getAddress());
            if(user.getLocTypeid()!=null){
            associateWordsDTO.setLocId(user.getLocTypeid());
            }
            associateWordsDTO.setIsAllDest(0);
            if(user.getHsSpeedRoomI()!=null){
                associateWordsDTO.setIsSpeedRoom(user.getHsSpeedRoomI());
            }
            associateWords.add(associateWordsDTO);
           }
        SimpleQuery query2 = new SimpleQuery(new Criteria(SolrSearchableUserFields.ADDRESS).is(words)); 
        query2.addCriteria(new Criteria(SolrSearchableUserFields.ID).is(values).not());
        //2为地址
        if(locid!=0){
            query2.addCriteria(new Criteria(SolrSearchableUserFields.LOCATION_TYPEID).is(locid));
        }
        if(destId!=0){
            query2.addCriteria(new Criteria(SolrSearchableUserFields.DEST_ID).is(destId));
        }
        query2.addCriteria(new Criteria(SolrSearchableUserFields.STATUS).is(1));
        query2.addCriteria(new Criteria(SolrSearchableUserFields.VERIFIED_BY_ZZK).is(1));
        //最多2条记录
        query2.setRows(10);
        
        Iterator<User> address=getSolrOperations().queryForPage(query2, User.class).iterator();
        /**
         * 有可能有重复的民宿 
         */
        while(address.hasNext()){
            //user.add(address.next());
            User user=address.next();
            if(user.getId()!=null&&ids.contains(user.getId())==false){
                AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
                if(user.getId()!=null){
                associateWordsDTO.setUid(user.getId());
                }
                associateWordsDTO.setAssociateType(AssociateType.HOMESTAY_ADDRESS);
                if(user.getDestId()!=null){
                associateWordsDTO.setDestId(user.getDestId());
                }
                associateWordsDTO.setName(user.getUsername()==null?"":user.getUsername());
                associateWordsDTO.setAddress(user.getAddress()==null?"":user.getAddress());
                if(user.getLocTypeid()!=null){
                associateWordsDTO.setLocId(user.getLocTypeid());
                }
                associateWordsDTO.setIsAllDest(0);
                if(user.getHsSpeedRoomI()!=null){
                    associateWordsDTO.setIsSpeedRoom(user.getHsSpeedRoomI());
                }
                associateWords.add(associateWordsDTO);
                }
             }
        LOG.info("when call queryUserByWordsAndLoc, use: {}ms", System.currentTimeMillis() - start);
        return associateWords;
    }
    
    @Override
    public List<AssociateWordsDTO> queryUserByWordsAndDest(String words,Integer destId) throws ZZKServiceException {
        long start = System.currentTimeMillis();
        if (words == null) {
            throw new IllegalParamterException("words is null");
        }
        if (destId == null) {
            throw new IllegalParamterException("destId is null");
        }
        List<AssociateWordsDTO> associateWords=new ArrayList<AssociateWordsDTO>();
        SimpleQuery query = new SimpleQuery(new Criteria(SolrSearchableUserFields.USERNAME).is(words));
        /*
         * 测试民宿 特例
         */   
        ArrayList<Integer> values=new ArrayList<Integer>();
        values.add(66);
        values.add(40080);
        values.add(40793);
        values.add(292734);  
        query.addCriteria(new Criteria(SolrSearchableUserFields.ID).is(values).not());
        if(destId!=0){
            query.addCriteria(new Criteria(SolrSearchableUserFields.DEST_ID).is(destId));
        }
        query.addCriteria(new Criteria(SolrSearchableUserFields.STATUS).is(1));
        //认证
        query.addCriteria(new Criteria(SolrSearchableUserFields.VERIFIED_BY_ZZK).is(1));
        //最多1条记录
        query.setRows(5);
        Iterator<User> username=getSolrOperations().queryForPage(query, User.class).iterator();
        int u=0;
        while(username.hasNext()){
            User user=username.next();
            AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
            if(user.getId()!=null){
            associateWordsDTO.setUid(user.getId());
            u=user.getId();
            }
            associateWordsDTO.setAssociateType(AssociateType.HOMESTAY_NAME);
            if(user.getDestId()!=null){
            associateWordsDTO.setDestId(user.getDestId());
            }
            associateWordsDTO.setName(user.getUsername()==null?"":user.getUsername());
            associateWordsDTO.setAddress(user.getAddress()==null?"":user.getAddress());
            if(user.getLocTypeid()!=null){
            associateWordsDTO.setLocId(user.getLocTypeid());
            }
            if(user.getHsSpeedRoomI()!=null){
                associateWordsDTO.setIsSpeedRoom(user.getHsSpeedRoomI());
            }
            associateWordsDTO.setIsAllDest(1);
            associateWords.add(associateWordsDTO);
           }
        //2为地址
        SimpleQuery query2 = new SimpleQuery(new Criteria(SolrSearchableUserFields.ADDRESS).is(words));
        query2.addCriteria(new Criteria(SolrSearchableUserFields.ID).is(values).not());
        if(destId!=0){
            query2.addCriteria(new Criteria(SolrSearchableUserFields.DEST_ID).is(destId));
        }
        query2.addCriteria(new Criteria(SolrSearchableUserFields.STATUS).is(1));
        query2.addCriteria(new Criteria(SolrSearchableUserFields.VERIFIED_BY_ZZK).is(1));
        //最多2条记录
        query2.setRows(5);    
        Iterator<User> address=getSolrOperations().queryForPage(query2, User.class).iterator();
        while(address.hasNext()){
            
            User user=address.next();
            if(user.getId()!=null&&user.getId()!=u){
                AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
                if(user.getId()!=null){
                associateWordsDTO.setUid(user.getId());
                }
                associateWordsDTO.setAssociateType(AssociateType.HOMESTAY_ADDRESS);
                if(user.getDestId()!=null){
                associateWordsDTO.setDestId(user.getDestId());
                }
                associateWordsDTO.setName(user.getUsername()==null?"":user.getUsername());
                associateWordsDTO.setAddress(user.getAddress()==null?"":user.getAddress());
                if(user.getLocTypeid()!=null){
                associateWordsDTO.setLocId(user.getLocTypeid());
                }
                associateWordsDTO.setIsAllDest(1);
                if(user.getHsSpeedRoomI()!=null){
                    associateWordsDTO.setIsSpeedRoom(user.getHsSpeedRoomI());
                }
                associateWords.add(associateWordsDTO);
                }
             }
        LOG.info("when call queryUserByWordsAndDest, use: {}ms", System.currentTimeMillis() - start);
        return associateWords;
    }

    @Override
    public PageList<com.zizaike.entity.solr.dto.User> serviceQuery(ServiceSearchVo serviceSearchVo) throws ZZKServiceException {
        long start = System.currentTimeMillis();
        if(serviceSearchVo==null){
            throw new IllegalParamterException("serviceSearchVo is null");
        }
        if(serviceSearchVo.getChannel()==null){
            throw new IllegalParamterException("channel is null");
        }
        if(serviceSearchVo.getDestId()==null){
            throw new IllegalParamterException("destId is null");
        }
        if(serviceSearchVo.getServiceType()==null){
            throw new IllegalParamterException("BnbServiceType is null");
        }
        com.zizaike.entity.solr.dto.User user = new com.zizaike.entity.solr.dto.User();
        BNBServiceSearchStatistics bnbServiceSearchStatistics = new BNBServiceSearchStatistics();
        bnbServiceSearchStatistics.setBnbServiceType(serviceSearchVo.getServiceType());
        bnbServiceSearchStatistics.setChannel(serviceSearchVo.getChannel());
        bnbServiceSearchStatistics.setDestId(serviceSearchVo.getDestId());
        applicationContext.publishEvent(new BNBServiceSearchApplicationEvent(bnbServiceSearchStatistics));
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.addSort("hs_comments_num_i", ORDER.desc);
        solrQuery.setStart((serviceSearchVo.getPage() - 1) * pageSize);
        /*
         * app端一页显示10
         */
        solrQuery.setRows(pageSize);
        StringBuffer filterQueries = new StringBuffer();
        filterQueries.append("dest_id:"+serviceSearchVo.getDestId());
        if(serviceSearchVo.getServiceType()==BNBServiceType.OUTDOORS){
            filterQueries.append("AND huwai_service_i:1");
        }else if(serviceSearchVo.getServiceType()==BNBServiceType.FOOD){
            filterQueries.append("AND zaocan_service_i:1");
        }else if(serviceSearchVo.getServiceType()==BNBServiceType.BOOKING){
            filterQueries.append("AND daiding_service_i:1");
        }else if(serviceSearchVo.getServiceType()==BNBServiceType.TRANSFER){
            filterQueries.append("AND jiesong_service_i:1");
        }else if(serviceSearchVo.getServiceType()==BNBServiceType.BUS_SERVICE){
            filterQueries.append("AND baoche_service_i:1");
        }else if(serviceSearchVo.getServiceType()==BNBServiceType.OTHER){
            filterQueries.append("AND other_service_i:1");
        }
        solrQuery.setFilterQueries(filterQueries.toString());
        DocumentObjectBinder binder = new DocumentObjectBinder();
        try {
            QueryResponse qr = getSolrOperations().getSolrServer().query(solrQuery);
            GroupResponse gr = qr.getGroupResponse();
            //匹配房间数目
            int matches = gr.getValues().get(0).getMatches();
            //匹配民宿数目
            int ngroups = gr.getValues().get(0).getNGroups();
            //内容
            List<Group> lg = gr.getValues().get(0).getValues();
        } catch (SolrServerException e) {
            e.printStackTrace();  
            LOG.error(" solr exception{}",e);
        }
        return null;
    }
}
