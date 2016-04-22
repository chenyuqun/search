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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.geo.Point;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zizaike.core.common.page.PageList;
import com.zizaike.core.framework.event.BusinessOperationBeforeEvent;
import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.BNBServiceSearchStatistics;
import com.zizaike.entity.solr.BNBServiceType;
import com.zizaike.entity.solr.Place;
import com.zizaike.entity.solr.SearchType;
import com.zizaike.entity.solr.ServiceSearchVo;
import com.zizaike.entity.solr.User;
import com.zizaike.entity.solr.dto.AssociateType;
import com.zizaike.entity.solr.dto.AssociateWordsDTO;
import com.zizaike.entity.solr.dto.BNBService;
import com.zizaike.entity.solr.dto.BNBServiceSolr;
import com.zizaike.entity.solr.model.SolrSearchableUserFields;
import com.zizaike.is.solr.PlaceSolrService;
import com.zizaike.is.solr.UserSolrService;
import com.zizaike.solr.bo.EventPublishService;
import com.zizaike.solr.domain.SearchBusinessOperation;

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
    public PlaceSolrService placeSolrService;
    @Autowired
    private EventPublishService eventPublishService;
    private static final Integer PAGE_SIZE = 10;
   //图片地址
    private static final String IMAGE_HOST = "http://img1.zzkcdn.com";
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
        if(serviceSearchVo.getSearchType()==null){
            throw new IllegalParamterException("SearchType is null");
        }
        if(serviceSearchVo.getSearchid()==null){
            throw new IllegalParamterException("searchId is null");
        }
        BNBServiceSearchStatistics bnbServiceSearchStatistics = new BNBServiceSearchStatistics();
        bnbServiceSearchStatistics.setBnbServiceType(serviceSearchVo.getServiceType());
        bnbServiceSearchStatistics.setChannel(serviceSearchVo.getChannel());
        bnbServiceSearchStatistics.setDestId(serviceSearchVo.getDestId());
        //服务查询事件
        BusinessOperationBeforeEvent<BNBServiceSearchStatistics> beforeEvent = new BusinessOperationBeforeEvent<BNBServiceSearchStatistics>(
                SearchBusinessOperation.SERVICE_SEARCH, bnbServiceSearchStatistics);
        eventPublishService.publishEvent(beforeEvent);
        SimpleQuery solrQuery = new SimpleQuery();
        solrQuery.addSort(new Sort(Sort.Direction.DESC,User.HS_COMMENTS_NUM_I_FIELD));
        solrQuery.setPageRequest(new PageRequest(serviceSearchVo.getPage(), PAGE_SIZE));
        solrQuery.addCriteria(new Criteria(User.DEST_ID_FIELD).is(serviceSearchVo.getDestId()));
        if(serviceSearchVo.getServiceType()==BNBServiceType.OUTDOORS){
            solrQuery.addCriteria(new Criteria(User.HUWAI_SERVICE_I_FIELD).is(1));
        }else if(serviceSearchVo.getServiceType()==BNBServiceType.FOOD){
            solrQuery.addCriteria(new Criteria(User.ZAOCAN_SERVICE_I_FIELD).is(1));
        }else if(serviceSearchVo.getServiceType()==BNBServiceType.BOOKING){
            solrQuery.addCriteria(new Criteria(User.DAIDING_SERVICE_I_FIELD).is(1));
        }else if(serviceSearchVo.getServiceType()==BNBServiceType.TRANSFER){
            solrQuery.addCriteria(new Criteria(User.JIESONG_SERVICE_I_FIELD).is(1));
        }else if(serviceSearchVo.getServiceType()==BNBServiceType.BUS_SERVICE){
            solrQuery.addCriteria(new Criteria(User.BAOCHE_SERVICE_I_FIELD).is(1));
        }else if(serviceSearchVo.getServiceType()==BNBServiceType.OTHER){
            solrQuery.addCriteria(new Criteria(User.OTHER_SERVICE_I_FIELD).is(1));
        }
        if (serviceSearchVo.getSearchType() == SearchType.BUSINES_CIRCLE
                || serviceSearchVo.getSearchType() == SearchType.BUSINESS_AREA
                || serviceSearchVo.getSearchType() == SearchType.SCENIC_SPOTS
                || serviceSearchVo.getSearchType() == SearchType.SPORTVAN
                ) {
            Place place = placeSolrService.queryPlaceById(serviceSearchVo.getSearchid());
            Point location = new Point(place.getGoogleMapLat(), place.getGoogleMapLng());
            solrQuery.addCriteria(new Criteria("latlng_p").near(location, new Distance(serviceSearchVo.getSearchRadius() != null ? serviceSearchVo.getSearchRadius() : place.getSearchRadius(),Metrics.KILOMETERS)));
        } else {
            if(serviceSearchVo.getSearchid()!=0){
                solrQuery.addCriteria(new Criteria(User.LOC_TYPEID_FIELD).is(serviceSearchVo.getSearchid()));
            }
        }
        PageList<com.zizaike.entity.solr.dto.User> pageList = new PageList<>();
            Page<com.zizaike.entity.solr.User> userS = getSolrOperations().queryForPage(solrQuery,User.class);
            LOG.debug("solrquery:{}", solrQuery);
            //内容
            List<com.zizaike.entity.solr.dto.User> userServices = new ArrayList<com.zizaike.entity.solr.dto.User>();
                for(User user: userS.getContent()){
                    com.zizaike.entity.solr.dto.User userService = new com.zizaike.entity.solr.dto.User();
                    userService.setId(user.getId());
                    String userPhoto = user.getUserPhotoFile();
                    //头像取小图
                    if (userPhoto!= null && userPhoto.contains("public/zzk_")) {
                        userService.setImage(IMAGE_HOST + "/" + userPhoto.substring(7) + "-userphotomedium.jpg");
                    } else if (userPhoto != "") {
                        userService.setImage(IMAGE_HOST + "/" + userPhoto + "/2000x1500.jpg-userphotomedium.jpg");
                    }
                    //取服务
                    JSONObject allServiceObject = JSON.parseObject(user.getAllServiceListS());
                    List<BNBServiceSolr> serviceList =  JSON.parseObject(allServiceObject.get(BNBServiceType.findSolrServiceName(serviceSearchVo.getServiceType())).toString(), new TypeReference<ArrayList<BNBServiceSolr>>(){});
                    List<BNBService> bnbServiceList = new ArrayList<BNBService>();
                    for (BNBServiceSolr bnbServiceSolr : serviceList) {
                        BNBService bnbService = new BNBService();
                        bnbService.setContent(bnbServiceSolr.getContent());
                        bnbService.setId(bnbServiceSolr.getId());
                        bnbService.setImages(bnbServiceSolr.getImages());
                        bnbService.setPrice(bnbServiceSolr.getPrice());
                        bnbService.setServiceType(BNBServiceType.findBySolrStr(bnbServiceSolr.getServiceCategory()));
                        bnbService.setServiceName(bnbServiceSolr.getTitle());
                        bnbServiceList.add(bnbService);
                    }
                    userService.setBnbService(bnbServiceList);
                    userService.setLocName(user.getLocTypename());
                    userService.setName(user.getUsername());
                    userServices.add(userService);
                }
                pageList.setList(userServices);
                com.zizaike.core.common.page.Page page = new com.zizaike.core.common.page.Page();
                page.setPageNo(userS.getNumber());
                page.setTotalCount(Integer.parseInt(userS.getTotalElements()+""));
                pageList.setPage(page);
        LOG.info("when call serviceQuery, use: {}ms", System.currentTimeMillis() - start);
        return pageList;
    }
}
