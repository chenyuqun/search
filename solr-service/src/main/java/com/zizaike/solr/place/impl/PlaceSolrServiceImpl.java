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
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.solr.Place;
import com.zizaike.entity.solr.model.SolrSearchablePlaceFields;
import com.zizaike.is.solr.PlaceSolrService;

@NoRepositoryBean
public class PlaceSolrServiceImpl extends SimpleSolrRepository<Place, Integer>  implements PlaceSolrService {
    protected final Logger LOG = LoggerFactory.getLogger(PlaceSolrServiceImpl.class);
    @Override
    public List<Place> queryPlaceByWords(String words) throws ZZKServiceException {
        long start = System.currentTimeMillis();
        if (words == null) {
            throw new IllegalParamterException("words is null");
        }
        ArrayList<Place> place=new ArrayList<Place>();
        SimpleQuery query = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).contains(words));       
        //1为商圈
        query.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(1));
        //最多1条记录
        query.setRows(1);
        Iterator<Place> shangquan=getSolrOperations().queryForPage(query, Place.class).iterator();            
        while(shangquan.hasNext()){
            place.add(shangquan.next());
           }
        SimpleQuery query2 = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).contains(words));       
        //2为景点
        query2.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(2));
        //最多1条记录
        query2.setRows(1);
        Iterator<Place> spot=getSolrOperations().queryForPage(query2, Place.class).iterator();
        while(spot.hasNext()){
            place.add(spot.next());
             }
        LOG.info("when call queryPlaceByWords, use: {}ms", System.currentTimeMillis() - start);
        return place;
    }
    
    @Override
    public List<Place> queryPlaceByWordsAndLoc(String words,Integer locid) throws ZZKServiceException {
        long start = System.currentTimeMillis();
        if (words == null) {
            throw new IllegalParamterException("words is null");
        }
        if (locid == null) {
            throw new IllegalParamterException("locid is null");
        }
        ArrayList<Place> place=new ArrayList<Place>();
        SimpleQuery query = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).contains(words));       
        //1为商圈
        query.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(1));
        //加上locId限制
        query.addCriteria(new Criteria(SolrSearchablePlaceFields.LOCID).is(locid));
        //最多2条记录
        query.setRows(2);
        Iterator<Place> shangquan=getSolrOperations().queryForPage(query, Place.class).iterator();            
        while(shangquan.hasNext()){
            place.add(shangquan.next());
           }
        SimpleQuery query2 = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).contains(words));       
        //2为景点
        query2.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(2));
        query2.addCriteria(new Criteria(SolrSearchablePlaceFields.LOCID).is(locid));
        //最多2条记录
        query2.setRows(2);
        
        Iterator<Place> spot=getSolrOperations().queryForPage(query2, Place.class).iterator();
        while(spot.hasNext()){
            place.add(spot.next());
             }
        LOG.info("when call queryPlaceByWordsAndLoc, use: {}ms", System.currentTimeMillis() - start);
        return place;
    }
}
  
