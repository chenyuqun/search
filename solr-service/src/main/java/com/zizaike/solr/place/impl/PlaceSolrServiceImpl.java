/**  
 * Project Name:solr-service  <br/>
 * File Name:PlaceSolrServiceImpl.java  <br/>
 * Package Name:com.zizaike.solr.user.impl  <br/>
 * Date:2015年11月3日下午6:04:18  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.solr.place.impl;  


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Crotch;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.hot.Loctype;
import com.zizaike.entity.solr.Place;
import com.zizaike.entity.solr.dto.AssociateType;
import com.zizaike.entity.solr.dto.AssociateWordsDTO;
import com.zizaike.entity.solr.model.SolrSearchablePlaceFields;
import com.zizaike.is.recommend.LoctypeService;
import com.zizaike.is.solr.PlaceSolrService;
import com.zizaike.is.solr.UserSolrService;

@NoRepositoryBean
public class PlaceSolrServiceImpl extends SimpleSolrRepository<Place, Integer>  implements PlaceSolrService {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceSolrServiceImpl.class);
    @Autowired
    public UserSolrService userSolrService; 
    @Autowired
    public LoctypeService loctypeService;
    
    @Override
    public List<Place> queryPlaceByWords(String words) throws ZZKServiceException {
        long start = System.currentTimeMillis();
        if (words == null) {
            throw new IllegalParamterException("words is null");
        }
        List<Place> place=new ArrayList<Place>();
        SimpleQuery query = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));       
        //1为商圈
        query.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(1));
        //最多1条记录
        query.setRows(1);
        Iterator<Place> shangquan=getSolrOperations().queryForPage(query, Place.class).iterator();            
        while(shangquan.hasNext()){
            place.add(shangquan.next());
           }
        SimpleQuery query2 = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));       
        //2为景点
        query2.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(2));
        //最多1条记录
        query2.setRows(1);
        Iterator<Place> spot=getSolrOperations().queryForPage(query2, Place.class).iterator();
        while(spot.hasNext()){
            place.add(spot.next());
             }
        //if(place.isEmpty());
        LOG.info("when call queryPlaceByWords, use: {}ms", System.currentTimeMillis() - start);
        return place;
    }
    
    @Override
    public List<AssociateWordsDTO> queryPlaceByWordsAndLoc(String words,Integer destId,Integer locid) throws ZZKServiceException {
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
        //查询城市 走database
        Loctype loctype=new Loctype();
        loctype.setDestId(destId);
        loctype.setTypeName(words);
        List<Loctype> loc=loctypeService.queryLoctype(loctype);
        for(int i=0;i<loc.size();i++){
            AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
            associateWordsDTO.setIsAllDest(0);
            associateWordsDTO.setAssociateType(AssociateType.CITY);
            if(loc.get(i).getLocid()!=null){
            associateWordsDTO.setLocId(loc.get(i).getLocid());
            }
            associateWordsDTO.setName(loc.get(i).getTypeName()==null?"":loc.get(i).getTypeName());
            associateWords.add(associateWordsDTO);
        }
        //List<Place> place=new ArrayList<Place>();
        SimpleQuery query = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));       
        //1为商圈
        query.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(1));    
        query.addCriteria(new Criteria(SolrSearchablePlaceFields.DEST_ID).is(destId));      
        //加上locId限制
        if(locid!=0){
            query.addCriteria(new Criteria(SolrSearchablePlaceFields.LOCID).is(locid));
        }
        //最多2条记录
        query.setRows(2);
        Iterator<Place> shangquan=getSolrOperations().queryForPage(query, Place.class).iterator();            
                while(shangquan.hasNext()){
            //place.add(shangquan.next());
            Place place=shangquan.next();
            AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
            if(place.getId()!=null){
            associateWordsDTO.setUid(place.getId());
            }
            associateWordsDTO.setAssociateType(AssociateType.BUSINESS_AREA);
            if(place.getDestId()!=null){
            associateWordsDTO.setDestId(place.getDestId());
            }
            associateWordsDTO.setName(place.getPoiName()==null?"":place.getPoiName());
            associateWordsDTO.setAddress(place.getGoogleMapAddress()==null?"":place.getGoogleMapAddress());
            if(place.getLocid()!=null){
            associateWordsDTO.setLocId(place.getLocid());
            }
            associateWordsDTO.setIsAllDest(0);
            associateWords.add(associateWordsDTO);
           }
        SimpleQuery query2 = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));       
        //2为景点
        query2.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(2));
        query2.addCriteria(new Criteria(SolrSearchablePlaceFields.DEST_ID).is(destId));
        if(locid!=0){
            query2.addCriteria(new Criteria(SolrSearchablePlaceFields.LOCID).is(locid));
        }
        //最多2条记录
        query2.setRows(2); 
        Iterator<Place> spot=getSolrOperations().queryForPage(query2, Place.class).iterator();
        while(spot.hasNext()){
            //place.add(spot.next());
            Place place=spot.next();
            AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
            if(place.getId()!=null){
            associateWordsDTO.setUid(place.getId());
            }
            associateWordsDTO.setAssociateType(AssociateType.SCENIC_SPOTS);
            if(place.getDestId()!=null){
            associateWordsDTO.setDestId(place.getDestId());
            }
            associateWordsDTO.setName(place.getPoiName()==null?"":place.getPoiName());
            associateWordsDTO.setAddress(place.getGoogleMapAddress()==null?"":place.getGoogleMapAddress());
            if(place.getLocid()!=null){
            associateWordsDTO.setLocId(place.getLocid());
            }
            associateWordsDTO.setIsAllDest(0);
            associateWords.add(associateWordsDTO);
             }
        //查询民宿名称和地址关联结果
        List<AssociateWordsDTO> user=userSolrService.queryUserByWordsAndLoc(words, destId, locid);
        associateWords.addAll(user);
        if(associateWords.size()>10){
            List<AssociateWordsDTO> newAssociateWords=new ArrayList<AssociateWordsDTO>();
            for(int i=0;i<10;i++){
                newAssociateWords.add(associateWords.get(i));
            }
           return newAssociateWords;
        }
        //如果没有结果则查询全站
        if(associateWords.size()==0){
            //城市 走database
            Loctype loctype2=new Loctype();
            loctype2.setTypeName(words);
            List<Loctype> locall=loctypeService.queryLoctype(loctype);
            for(int i=0;i<locall.size();i++){
                AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
                associateWordsDTO.setIsAllDest(0);
                associateWordsDTO.setAssociateType(AssociateType.CITY);
                if(loc.get(i).getLocid()!=null){
                associateWordsDTO.setLocId(locall.get(i).getLocid());
                }
                associateWordsDTO.setName(locall.get(i).getTypeName()==null?"":loc.get(i).getTypeName());
                associateWords.add(associateWordsDTO);
            }
            //全站商圈
            SimpleQuery query3 = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));
            query3.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(1));
            query3.setRows(1); 
            shangquan=getSolrOperations().queryForPage(query3, Place.class).iterator(); 
            while(shangquan.hasNext()){
                Place place=shangquan.next();
                AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
                if(place.getId()!=null){
                associateWordsDTO.setUid(place.getId());
                }
                associateWordsDTO.setAssociateType(AssociateType.BUSINESS_AREA);
                if(place.getDestId()!=null){
                associateWordsDTO.setDestId(place.getDestId());
                }
                associateWordsDTO.setName(place.getPoiName()==null?"":place.getPoiName());
                associateWordsDTO.setAddress(place.getGoogleMapAddress()==null?"":place.getGoogleMapAddress());
                if(place.getLocid()!=null){
                associateWordsDTO.setLocId(place.getLocid());
                }
                associateWordsDTO.setIsAllDest(1);
                associateWords.add(associateWordsDTO);
            }
            //全站景点
            SimpleQuery query4 = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));
            query4.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(2));
            query4.setRows(1); 
            spot=getSolrOperations().queryForPage(query4, Place.class).iterator();
            while(shangquan.hasNext()){
                Place place=spot.next();
                AssociateWordsDTO  associateWordsDTO=new AssociateWordsDTO();
                if(place.getId()!=null){
                associateWordsDTO.setUid(place.getId());
                }
                associateWordsDTO.setAssociateType(AssociateType.SCENIC_SPOTS);
                if(place.getDestId()!=null){
                associateWordsDTO.setDestId(place.getDestId());
                }
                associateWordsDTO.setName(place.getPoiName()==null?"":place.getPoiName());
                associateWordsDTO.setAddress(place.getGoogleMapAddress()==null?"":place.getGoogleMapAddress());
                if(place.getLocid()!=null){
                associateWordsDTO.setLocId(place.getLocid());
                }
                associateWordsDTO.setIsAllDest(1);
                associateWords.add(associateWordsDTO);
            }
            //全站民宿 
            List<AssociateWordsDTO> user2=userSolrService.queryUserByWordsAndDest(words, destId);
            associateWords.addAll(user2);
            //最多5条
            if(associateWords.size()>5){
                List<AssociateWordsDTO> newAssociateWords=new ArrayList<AssociateWordsDTO>();
                for(int i=0;i<5;i++){
                    newAssociateWords.add(associateWords.get(i));
                }
               return newAssociateWords;
            }
        }
        LOG.info("when call queryPlaceByWordsAndLoc, use: {}ms", System.currentTimeMillis() - start);
        return associateWords;
    }
    
    
}
  
