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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.solr.User;
import com.zizaike.entity.solr.dto.AssociateWordsDTO;
import com.zizaike.entity.solr.model.SolrSearchableUserFields;
import com.zizaike.is.solr.UserSolrService;

/**
 * ClassName:UserServiceImpl <br/>
 * Function: 用户查询实现. <br/>
 * Reason:用户查询. <br/>
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
        SimpleQuery query = new SimpleQuery(new Criteria(SolrSearchableUserFields.USERNAME).contains(words));       
        //1为商圈
        if(locid!=0){
            query.addCriteria(new Criteria(SolrSearchableUserFields.LOC_TYPEID).is(locid));
        }
        //加上locId限制
        query.addCriteria(new Criteria(SolrSearchableUserFields.DEST_ID).is(destId));
        //最多2条记录
        query.setRows(2);
        Iterator<User> username=getSolrOperations().queryForPage(query, User.class).iterator();            
        while(username.hasNext()){
            //user.add(username.next());
            User user=username.next();
            AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
            if(user.getId()!=null){
            associateWordsDTO.setUid(user.getId());
            }
            associateWordsDTO.setAssociateType(4);
            if(user.getDestId()!=null){
            associateWordsDTO.setDestId(user.getDestId());
            }
            associateWordsDTO.setName(user.getUsername()==null?"":user.getUsername());
            associateWordsDTO.setAddress(user.getAddress()==null?"":user.getAddress());
            if(user.getLocTypeid()!=null){
            associateWordsDTO.setLocId(user.getLocTypeid());
            }
            associateWordsDTO.setIsAllDest(0);
            associateWords.add(associateWordsDTO);
           }
        SimpleQuery query2 = new SimpleQuery(new Criteria(SolrSearchableUserFields.ADDRESS).contains(words));       
        //2为地址
        if(locid!=0){
            query2.addCriteria(new Criteria(SolrSearchableUserFields.LOC_TYPEID).is(locid));
        }
        query2.addCriteria(new Criteria(SolrSearchableUserFields.DEST_ID).is(destId));
        //最多2条记录
        query2.setRows(10);
        
        Iterator<User> address=getSolrOperations().queryForPage(query2, User.class).iterator();
        while(address.hasNext()){
            //user.add(address.next());
            User user=address.next();
            AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
            if(user.getId()!=null){
            associateWordsDTO.setUid(user.getId());
            }
            associateWordsDTO.setAssociateType(5);
            if(user.getDestId()!=null){
            associateWordsDTO.setDestId(user.getDestId());
            }
            associateWordsDTO.setName(user.getUsername()==null?"":user.getUsername());
            associateWordsDTO.setAddress(user.getAddress()==null?"":user.getAddress());
            if(user.getLocTypeid()!=null){
            associateWordsDTO.setLocId(user.getLocTypeid());
            }
            associateWordsDTO.setIsAllDest(0);
            associateWords.add(associateWordsDTO);
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
        SimpleQuery query = new SimpleQuery(new Criteria(SolrSearchableUserFields.USERNAME).contains(words));
        query.addCriteria(new Criteria(SolrSearchableUserFields.DEST_ID).is(destId));
        //最多1条记录
        query.setRows(1);
        Iterator<User> username=getSolrOperations().queryForPage(query, User.class).iterator();            
        while(username.hasNext()){
            User user=username.next();
            AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
            if(user.getId()!=null){
            associateWordsDTO.setUid(user.getId());
            }
            associateWordsDTO.setAssociateType(4);
            if(user.getDestId()!=null){
            associateWordsDTO.setDestId(user.getDestId());
            }
            associateWordsDTO.setName(user.getUsername()==null?"":user.getUsername());
            associateWordsDTO.setAddress(user.getAddress()==null?"":user.getAddress());
            if(user.getLocTypeid()!=null){
            associateWordsDTO.setLocId(user.getLocTypeid());
            }
            associateWordsDTO.setIsAllDest(1);
            associateWords.add(associateWordsDTO);
           }
        //2为地址
        SimpleQuery query2 = new SimpleQuery(new Criteria(SolrSearchableUserFields.ADDRESS).contains(words));   
        query2.addCriteria(new Criteria(SolrSearchableUserFields.DEST_ID).is(destId));
        //最多2条记录
        query2.setRows(5);    
        Iterator<User> address=getSolrOperations().queryForPage(query2, User.class).iterator();
        while(address.hasNext()){
            
            User user=address.next();
            AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
            if(user.getId()!=null){
            associateWordsDTO.setUid(user.getId());
            }
            associateWordsDTO.setAssociateType(5);
            if(user.getDestId()!=null){
            associateWordsDTO.setDestId(user.getDestId());
            }
            associateWordsDTO.setName(user.getUsername()==null?"":user.getUsername());
            associateWordsDTO.setAddress(user.getAddress()==null?"":user.getAddress());
            if(user.getLocTypeid()!=null){
            associateWordsDTO.setLocId(user.getLocTypeid());
            }
            associateWordsDTO.setIsAllDest(1);
            associateWords.add(associateWordsDTO);
             }
        LOG.info("when call queryUserByWordsAndDest, use: {}ms", System.currentTimeMillis() - start);
        return associateWords;
    }
}
