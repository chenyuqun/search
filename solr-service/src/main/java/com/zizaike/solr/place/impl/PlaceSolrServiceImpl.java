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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.LocPoi;
import com.zizaike.entity.recommend.Loctype;
import com.zizaike.entity.solr.Place;
import com.zizaike.entity.solr.dto.AssociateType;
import com.zizaike.entity.solr.dto.AssociateWordAndSearchCondition;
import com.zizaike.entity.solr.dto.AssociateWordsDTO;
import com.zizaike.entity.solr.dto.PlaceDTO;
import com.zizaike.entity.solr.model.SolrSearchablePlaceFields;
import com.zizaike.entity.solr.type.PoiType;
import com.zizaike.is.common.HanLPService;
import com.zizaike.is.recommend.LocPoiService;
import com.zizaike.is.recommend.LoctypeService;
import com.zizaike.is.solr.PlaceSolrService;
import com.zizaike.is.solr.UserSolrService;

@NoRepositoryBean
public class PlaceSolrServiceImpl extends SimpleSolrRepository<Place, Integer> implements PlaceSolrService {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceSolrServiceImpl.class);
    @Autowired
    public UserSolrService userSolrService;
    @Autowired
    public LoctypeService loctypeService;
    @Autowired
    public LocPoiService locPoiService;
    @Autowired
    private HanLPService hanLPService;

    private static final Integer MAX_ROWS = 1000;

    @Override
    public List<Place> queryPlaceByWords(String words) throws ZZKServiceException {
        long start = System.currentTimeMillis();
        if (words == null) {
            throw new IllegalParamterException("words is null");
        }
        List<Place> place = new ArrayList<Place>();
        SimpleQuery query = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));
        // 1为商圈
        query.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(1));
        // 最多1条记录
        query.setRows(1);
        Iterator<Place> shangquan = getSolrOperations().queryForPage(query, Place.class).iterator();
        while (shangquan.hasNext()) {
            place.add(shangquan.next());
        }
        SimpleQuery query2 = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));
        // 2为景点
        query2.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(2));
        // 最多1条记录
        query2.setRows(1);
        Iterator<Place> spot = getSolrOperations().queryForPage(query2, Place.class).iterator();
        while (spot.hasNext()) {
            place.add(spot.next());
        }
        // if(place.isEmpty());
        LOG.info("when call queryPlaceByWords, use: {}ms", System.currentTimeMillis() - start);
        return place;
    }

    @Override
    public List<AssociateWordsDTO> queryPlaceByWordsAndLoc(String words, Integer destId, Integer locid)
            throws ZZKServiceException {
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
        // 繁体转成简体
        words = hanLPService.convertToSimplifiedChinese(words);
        List<AssociateWordsDTO> associateWords = new ArrayList<AssociateWordsDTO>();
        // 查询城市 走database
        Loctype loctype = new Loctype();
        loctype.setDestId(destId);
        loctype.setTypeName(words);
        List<Loctype> loc = loctypeService.queryLoctype(loctype);
        for (int i = 0; i < loc.size(); i++) {
            AssociateWordsDTO associateWordsDTO = new AssociateWordsDTO();
            associateWordsDTO.setIsAllDest(0);
            associateWordsDTO.setAssociateType(AssociateType.CITY);
            if (loc.get(i).getLocid() != null) {
                associateWordsDTO.setLocId(loc.get(i).getLocid());
            }
            associateWordsDTO.setName(loc.get(i).getTypeName() == null ? "" : loc.get(i).getTypeName());
            associateWords.add(associateWordsDTO);
        }
        // List<Place> place=new ArrayList<Place>();
        SimpleQuery query = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));
        // 1为商圈
        query.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(1));
        if (destId != 0) {
            query.addCriteria(new Criteria(SolrSearchablePlaceFields.DEST_ID).is(destId));
        }
        // 1为有效
        query.addCriteria(new Criteria(SolrSearchablePlaceFields.STATUS).is(1));
        // 加上locId限制
        if (locid != 0) {
            query.addCriteria(new Criteria(SolrSearchablePlaceFields.LOCID).is(locid));
        }
        // 最多2条记录
        query.setRows(2);
        Iterator<Place> shangquan = getSolrOperations().queryForPage(query, Place.class).iterator();
        while (shangquan.hasNext()) {
            // place.add(shangquan.next());
            Place place = shangquan.next();
            AssociateWordsDTO associateWordsDTO = new AssociateWordsDTO();
            if (place.getId() != null) {
                associateWordsDTO.setUid(place.getId());
            }
            associateWordsDTO.setAssociateType(AssociateType.BUSINESS_AREA);
            if (place.getDestId() != null) {
                associateWordsDTO.setDestId(place.getDestId());
            }
            associateWordsDTO.setName(place.getPoiName() == null ? "" : place.getPoiName());
            associateWordsDTO.setAddress(place.getGoogleMapAddress() == null ? "" : place.getGoogleMapAddress());
            if (place.getLocid() != null) {
                associateWordsDTO.setLocId(place.getLocid());
            }
            associateWordsDTO.setIsAllDest(0);
            associateWords.add(associateWordsDTO);
        }
        SimpleQuery query2 = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));
        // 2为景点
        query2.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(2));
        if (destId != 0) {
            query2.addCriteria(new Criteria(SolrSearchablePlaceFields.DEST_ID).is(destId));
        }
        query2.addCriteria(new Criteria(SolrSearchablePlaceFields.STATUS).is(1));
        if (locid != 0) {
            query2.addCriteria(new Criteria(SolrSearchablePlaceFields.LOCID).is(locid));
        }
        // 最多2条记录
        query2.setRows(2);
        Iterator<Place> spot = getSolrOperations().queryForPage(query2, Place.class).iterator();
        while (spot.hasNext()) {
            // place.add(spot.next());
            Place place = spot.next();
            AssociateWordsDTO associateWordsDTO = new AssociateWordsDTO();
            if (place.getId() != null) {
                associateWordsDTO.setUid(place.getId());
            }
            associateWordsDTO.setAssociateType(AssociateType.SCENIC_SPOTS);
            if (place.getDestId() != null) {
                associateWordsDTO.setDestId(place.getDestId());
            }
            associateWordsDTO.setName(place.getPoiName() == null ? "" : place.getPoiName());
            associateWordsDTO.setAddress(place.getGoogleMapAddress() == null ? "" : place.getGoogleMapAddress());
            if (place.getLocid() != null) {
                associateWordsDTO.setLocId(place.getLocid());
            }
            associateWordsDTO.setIsAllDest(0);
            associateWords.add(associateWordsDTO);
        }
        // 查询民宿名称和地址关联结果
        List<AssociateWordsDTO> user = userSolrService.queryUserByWordsAndLoc(words, destId, locid);
        associateWords.addAll(user);
        if (associateWords.size() > 10) {
            List<AssociateWordsDTO> newAssociateWords = new ArrayList<AssociateWordsDTO>();
            for (int i = 0; i < 10; i++) {
                newAssociateWords.add(associateWords.get(i));
            }
            return newAssociateWords;
        }
        // 如果没有结果则查询全站
        if (associateWords.size() == 0) {
            // 城市 走database
            Loctype loctype2 = new Loctype();
            loctype2.setTypeName(words);
            List<Loctype> locall = loctypeService.queryLoctype(loctype2);
            for (int i = 0; i < locall.size(); i++) {
                AssociateWordsDTO associateWordsDTO = new AssociateWordsDTO();
                associateWordsDTO.setIsAllDest(1);
                associateWordsDTO.setAssociateType(AssociateType.CITY);
                if (loc.get(i).getLocid() != null) {
                    associateWordsDTO.setLocId(locall.get(i).getLocid());
                }
                associateWordsDTO.setName(locall.get(i).getTypeName() == null ? "" : loc.get(i).getTypeName());
                associateWords.add(associateWordsDTO);
            }
            // 全站商圈
            SimpleQuery query3 = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));
            query3.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(1));
            query3.addCriteria(new Criteria(SolrSearchablePlaceFields.STATUS).is(1));
            /*
             * 全站搜索只到台湾一级
             */
            query3.addCriteria(new Criteria(SolrSearchablePlaceFields.DEST_ID).is(destId));
            query3.setRows(1);
            shangquan = getSolrOperations().queryForPage(query3, Place.class).iterator();
            while (shangquan.hasNext()) {
                Place place = shangquan.next();
                AssociateWordsDTO associateWordsDTO = new AssociateWordsDTO();
                if (place.getId() != null) {
                    associateWordsDTO.setUid(place.getId());
                }
                associateWordsDTO.setAssociateType(AssociateType.BUSINESS_AREA);
                if (place.getDestId() != null) {
                    associateWordsDTO.setDestId(place.getDestId());
                }
                associateWordsDTO.setName(place.getPoiName() == null ? "" : place.getPoiName());
                associateWordsDTO.setAddress(place.getGoogleMapAddress() == null ? "" : place.getGoogleMapAddress());
                if (place.getLocid() != null) {
                    associateWordsDTO.setLocId(place.getLocid());
                }
                associateWordsDTO.setIsAllDest(1);
                associateWords.add(associateWordsDTO);
            }
            // 全站景点
            SimpleQuery query4 = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.POI_NAME).is(words));
            query4.addCriteria(new Criteria(SolrSearchablePlaceFields.POI_TYPE).is(2));
            query4.addCriteria(new Criteria(SolrSearchablePlaceFields.STATUS).is(1));
            query4.addCriteria(new Criteria(SolrSearchablePlaceFields.DEST_ID).is(destId));
            query4.setRows(1);
            spot = getSolrOperations().queryForPage(query4, Place.class).iterator();
            while (shangquan.hasNext()) {
                Place place = spot.next();
                AssociateWordsDTO associateWordsDTO = new AssociateWordsDTO();
                if (place.getId() != null) {
                    associateWordsDTO.setUid(place.getId());
                }
                associateWordsDTO.setAssociateType(AssociateType.SCENIC_SPOTS);
                if (place.getDestId() != null) {
                    associateWordsDTO.setDestId(place.getDestId());
                }
                associateWordsDTO.setName(place.getPoiName() == null ? "" : place.getPoiName());
                associateWordsDTO.setAddress(place.getGoogleMapAddress() == null ? "" : place.getGoogleMapAddress());
                if (place.getLocid() != null) {
                    associateWordsDTO.setLocId(place.getLocid());
                }
                associateWordsDTO.setIsAllDest(1);
                associateWords.add(associateWordsDTO);
            }
            // 全站民宿
            List<AssociateWordsDTO> user2 = userSolrService.queryUserByWordsAndDest(words, destId);
            associateWords.addAll(user2);
            // 最多5条
            if (associateWords.size() > 5) {
                List<AssociateWordsDTO> newAssociateWords = new ArrayList<AssociateWordsDTO>();
                for (int i = 0; i < 5; i++) {
                    newAssociateWords.add(associateWords.get(i));
                }
                return newAssociateWords;
            }
        }
        LOG.info("when call queryPlaceByWordsAndLoc, use: {}ms", System.currentTimeMillis() - start);
        return associateWords;
    }

    @Override
    public List<PlaceDTO> queryPlaceByLocId(Integer locid) throws ZZKServiceException {
        // 全站商圈
        SimpleQuery queryByLocid = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.LOCID).is(locid));
        queryByLocid.addCriteria(new Criteria(SolrSearchablePlaceFields.STATUS).is(1));
        queryByLocid.setRows(MAX_ROWS);// 设置最大来保证取出所有数据
        Iterator<Place> placeList = getSolrOperations().queryForPage(queryByLocid, Place.class).iterator();
        List<PlaceDTO> list = new ArrayList<PlaceDTO>();

        for (Iterator<Place> iterator = placeList; iterator.hasNext();) {
            Place place = (Place) iterator.next();
            PlaceDTO placeDTO = new PlaceDTO();
            placeDTO.setId(place.getId());
            placeDTO.setPoiName(place.getPoiName());
            placeDTO.setPoiType(PoiType.findByValue(place.getPoiType()));
            list.add(placeDTO);
        }
        return list;
    }

    @Override
    public Place queryPlaceById(Integer id) throws ZZKServiceException {
        // 根据ID获取place
        SimpleQuery queryById = new SimpleQuery(new Criteria(SolrSearchablePlaceFields.ID).is(id));
        queryById.addCriteria(new Criteria(SolrSearchablePlaceFields.STATUS).is(1));
        Place place = getSolrOperations().queryForObject(queryById, Place.class);
        return place;
    }

    @Override
    public AssociateWordAndSearchCondition queryPlaceByWordsAndLocCodition(String words, Integer destId, Integer locid)
            throws ZZKServiceException {
        AssociateWordAndSearchCondition condition = new AssociateWordAndSearchCondition();
        condition.setAssociateWords(queryPlaceByWordsAndLoc(words, destId, locid));
        if (StringUtils.isEmpty(words)) {
            return condition;
        }
        List<LocPoi> list = locPoiService.queryLocPoi(words);
        if (list == null || list.size() == 0) {
            return condition;
        }
        Map searchCondition = new HashMap();
        LocPoi locPoi = list.get(0);
        searchCondition.put("searchid", locPoi.getId());
        searchCondition.put("searchType", AssociateType.findByValue(locPoi.getPoiType()));
        searchCondition.put("keyWords", "");
        condition.setSearchCondition(searchCondition);
        return condition;
    }

}
