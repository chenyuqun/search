/**  
 * Project Name:solr-service  <br/>
 * File Name:RoomSolrServiceImpl.java  <br/>
 * Package Name:com.zizaike.solr.room.impl  <br/>
 * Date:2015年11月6日下午2:16:08  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.solr.room.impl;

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
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.GroupParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.solr.Place;
import com.zizaike.entity.solr.Room;
import com.zizaike.entity.solr.RoomList;
import com.zizaike.entity.solr.RoomSolr;
import com.zizaike.entity.solr.SearchType;
import com.zizaike.entity.solr.SearchWordsVo;
import com.zizaike.entity.solr.model.SolrSearchableRoomFields;
import com.zizaike.is.solr.PlaceSolrService;
import com.zizaike.is.solr.RoomSolrService;
//import com.zizaike.solr.tools.SolrDoucmentTools;

/**  
 * ClassName: RoomSolrServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2015年11月6日 下午2:16:08 <br/>  
 *  
 * @author alex  
 * @version   
 * @since JDK 1.7  
 */
@NoRepositoryBean
public class RoomSolrServiceImpl extends SimpleSolrRepository<Room, Integer>  implements RoomSolrService {
    protected final Logger LOG = LoggerFactory.getLogger(RoomSolrServiceImpl.class);
    @Autowired
    public PlaceSolrService placeSolrService; 
    
    
    @Override
    public List<Room> queryRoomByWords(String words,int locTypeid) throws ZZKServiceException {
        long start = System.currentTimeMillis();
        if (words == null) {
            throw new IllegalParamterException("words is null");
        }
        List<Room> room=new ArrayList<Room>();
        SimpleQuery query = new SimpleQuery(new Criteria(SolrSearchableRoomFields.USERNAME).contains(words));
        query.addCriteria(new Criteria(SolrSearchableRoomFields.LOC_TYPEID).is(locTypeid));
        query.setRows(2);
        //最多2条记录
        //ScoredPage<Room> list = this.getSolrOperations().queryForPage(query, Room.class);
        Iterator<Room> username=getSolrOperations().queryForPage(query, Room.class).iterator();            
        while(username.hasNext()){
            room.add(username.next());
           }
        SimpleQuery query2 = new SimpleQuery(new Criteria(SolrSearchableRoomFields.USER_ADDRESS).contains(words));       
        //2为景点
        query2.addCriteria(new Criteria(SolrSearchableRoomFields.LOC_TYPEID).is(locTypeid));
        //最多1条记录
        query2.setRows(10);
        Iterator<Room> address=getSolrOperations().queryForPage(query2, Room.class).iterator();
        while(address.hasNext()){
            room.add(address.next());
             }
        LOG.info("when call queryRoomByWords, use: {}ms", System.currentTimeMillis() - start);
        return room;
    }
    
    @Override
    public RoomSolr searchSolr(SearchWordsVo searchWordsVo) throws ZZKServiceException{
        long start = System.currentTimeMillis();
        /**
         * search 1普通查询 地点关联 2 poi查询 坐标关联
         */
        int searchType=1;
        if(searchWordsVo.getSearchType()==SearchType.BUSINES_CIRCLE
                ||searchWordsVo.getSearchType()==SearchType.BUSINESS_AREA
                ||searchWordsVo.getSearchType()==SearchType.SCENIC_SPOTS
                ||searchWordsVo.getSearchType()==SearchType.SPORTVAN
                ){
            searchType=2;
        }
        /**
         * 获取坐标信息
         */
        String geoSort="";
        String geoFq="";
        String geoFl="";
        if(searchType==2){
            Place place=placeSolrService.queryPlaceById(searchWordsVo.getSearchid());
            geoSort="div(score_f, map(geodist(latlng_p,"+place.getGoogleMapLat()+","+place.getGoogleMapLng()+"),0,1,1))";
            geoFq="{!geofilt pt="+place.getGoogleMapLat()+","+place.getGoogleMapLng()+" sfield=latlng_p d=10}";
            geoFl="*, distance:geodist(latlng_p, "+place.getGoogleMapLat()+","+place.getGoogleMapLng()+")";
        }
        if(searchWordsVo.getKeyWords()==""||searchWordsVo.getKeyWords()==null||searchWordsVo.getKeyWords().isEmpty()){
            searchWordsVo.setKeyWords("*:*");
        }
        SolrQuery solrquery=new SolrQuery(); 
        solrquery.set(CommonParams.Q, searchWordsVo.getKeyWords());
        solrquery.set(CommonParams.WT,"json");
        solrquery.set("q.op","OR");
        /*
         * DisMax Conditions
         */
        solrquery.set(DisMaxParams.MM, "1");
        solrquery.set(DisMaxParams.PF, "username^1000 user_address^3000");
        solrquery.set(DisMaxParams.QF, "username^10 user_address^30");
        solrquery.set("defType","edismax");
        /*
         * Sort Conditions
         */
        solrquery.setSort("verified_by_zzk", ORDER.desc);
        solrquery.addSort("score", ORDER.desc);
        /*
         * 2种搜索方式
         */
        if(searchType==2
                ){
            solrquery.addSort(geoSort, ORDER.desc);
        }else{
            solrquery.addSort("score_f", ORDER.desc);
        }
        solrquery.addSort("changed", ORDER.desc);
        /*
         * Page Conditions
         */
        solrquery.setStart((searchWordsVo.getPage()-1));
        solrquery.setRows(25);
        /*
         * Filter Conditions
         */
        StringBuffer filterQueries=new StringBuffer();
        filterQueries.append("status:1 AND id:[0 TO 200000000] AND verified_by_zzk:1 AND -uid:(66 40080 40793 292734)");
       // solrquery.setFilterQueries("status:1 AND id:[0 TO 200000000] AND verified_by_zzk:1 AND -uid:(66 40080 40793 292734)");
    //    solrquery.setFilterQueries("status:1 AND dest_id:10 AND (*:* AND NOT soldout_room_dates_ss:(1119 OR 1120)) AND id:[0 TO 200000000] AND verified_by_zzk:1 AND -uid:(66 40080 40793 292734) AND {!geofilt pt=25.022167,121.528242 sfield=latlng_p d=10}");
        if(searchWordsVo.getDestId()!=0){
            filterQueries.append(" AND dest_id:"+searchWordsVo.getDestId()+"");
        }
        if(searchWordsVo.getCheckInDate()!=null&&searchWordsVo.getCheckOutDate()!=null
                &&(Integer.parseInt(searchWordsVo.getCheckOutDate())-Integer.parseInt(searchWordsVo.getCheckInDate())>0)){
            int i=Integer.parseInt(searchWordsVo.getCheckOutDate())-Integer.parseInt(searchWordsVo.getCheckInDate());
            int checkInDate=Integer.parseInt(searchWordsVo.getCheckInDate());
            StringBuffer sb=new StringBuffer();
            sb.append(checkInDate);
            for(int j=1;j<i;j++){
                checkInDate++;
                sb.append(" OR "+checkInDate);
            }
           
            filterQueries.append(" AND (*:* AND NOT soldout_room_dates_ss:("+sb+"))");
        }
        if(searchType==1){
            filterQueries.append(" AND location_typeid:"+searchWordsVo.getSearchid()+"");
        }
        if(searchType==2){
            filterQueries.append(" AND "+geoFq);
        }
        solrquery.setFilterQueries(filterQueries.toString());
        /*
         * Group Conditions
         */ 
        solrquery.set(GroupParams.GROUP_TOTAL_COUNT, true);
        solrquery.set(GroupParams.GROUP_FIELD, "uid");
        solrquery.set(GroupParams.GROUP_FORMAT, "grouped");
        solrquery.set(GroupParams.GROUP, true);
        solrquery.set(GroupParams.GROUP_OFFSET, 0);
        solrquery.set(GroupParams.GROUP_LIMIT, 100);
        if(searchType==2){
            solrquery.set(GroupParams.GROUP_SORT, "verified_by_zzk desc, score desc, "+geoSort+" desc, changed desc");
        }else{
            solrquery.set(GroupParams.GROUP_SORT, "verified_by_zzk desc, score desc, score_f desc, changed desc");
        }
        /*
         * fl
         */
        if(searchType==2){
        solrquery.set(CommonParams.FL, geoFl);
        }
        //SolrTemplate solrTemplate=new SolrTemplate(this.getSolrOperations().getSolrServer());
        //SolrQuery solrQuery = new QueryParsers().getForClass(query.getClass()).constructSolrQuery(query);
        DocumentObjectBinder binder = new DocumentObjectBinder(); 
        RoomSolr roomSolr=new RoomSolr();
        roomSolr.setMatches(0);
        roomSolr.setNgroups(0);
        try {
            QueryResponse qr=getSolrOperations().getSolrServer().query(solrquery);
            //qr.getResponse();
            GroupResponse gr=qr.getGroupResponse();
           
            //匹配房间数目
            int matches=gr.getValues().get(0).getMatches();
            roomSolr.setMatches(matches);
            //匹配民宿数目
            int ngroups=gr.getValues().get(0).getNGroups();
            roomSolr.setNgroups(ngroups);
            //内容
            List<Group> lg=gr.getValues().get(0).getValues();
           // roomSolr.setRoomGroup(roomGroup);
            
            List<RoomList> lsRoomList=new ArrayList<RoomList>();
            if(matches>0){
               
            
                for(int i=0;i<lg.size();i++){
                    RoomList roomList=new RoomList();
                    //民宿uid
                    roomList.setUid(Integer.parseInt(lg.get(i).getGroupValue()));
                    //每个民宿对应的房间信息
                    List<Room> lr=binder.getBeans(Room.class, lg.get(i).getResult());    
                    roomList.setRoomList(lr);
                    lsRoomList.add(roomList);
                }
            }
            roomSolr.setRoomGroup(lsRoomList);
        } catch (SolrServerException e) {             
            // TODO Auto-generated catch block  
            e.printStackTrace();            
        }
        LOG.info("when call searchSolr, use: {}ms", System.currentTimeMillis() - start);
        return roomSolr;
    }
    
    
    
    
}     
