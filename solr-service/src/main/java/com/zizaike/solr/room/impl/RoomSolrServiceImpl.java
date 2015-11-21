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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.GroupParams;
import org.apache.solr.parser.QueryParser;
import org.apache.solr.search.DisMaxQParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.QueryParsers;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.Function;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.Query.Operator;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.StatsOptions;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.hot.RecommendType;
import com.zizaike.entity.solr.Room;
import com.zizaike.entity.solr.RoomList;
import com.zizaike.entity.solr.RoomSolr;
import com.zizaike.entity.solr.SearchWordsVo;
import com.zizaike.entity.solr.User;
import com.zizaike.entity.solr.model.SolrSearchableRoomFields;
import com.zizaike.is.solr.RoomSolrService;
import com.zizaike.solr.tools.SolrDocumentTool;
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
//        if (words == null) {
//            throw new IllegalParamterException("words is null");
//        }
        if(searchWordsVo.getKeyWords() ==""){
            searchWordsVo.setKeyWords("*:*");
        }
        SolrQuery solrquery=new SolrQuery(); 
        solrquery.set(CommonParams.Q, searchWordsVo.getKeyWords());
        solrquery.set(CommonParams.WT,"json");
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
        if(searchWordsVo.getRecommendType()==RecommendType.BUSINES_CIRCLE){
            solrquery.addSort("div(score_f, map(geodist(latlng_p,25.022167,121.528242),0,1,1))", ORDER.desc);
        }else{
            solrquery.addSort("score_f", ORDER.desc);
        }
        solrquery.addSort("changed", ORDER.desc);
        /*
         * Page Conditions
         */
        solrquery.setStart(0);
        solrquery.setRows(25);
        /*
         * Filter Conditions
         */
        solrquery.setFilterQueries("status:1 AND dest_id:10 AND (*:* AND NOT soldout_room_dates_ss:(1119 OR 1120)) AND id:[0 TO 200000000] AND verified_by_zzk:1 AND -uid:(66 40080 40793 292734) AND {!geofilt pt=25.022167,121.528242 sfield=latlng_p d=10}");
        /*
         * Group Conditions
         */
        solrquery.set(GroupParams.GROUP_TOTAL_COUNT, true);
        solrquery.set(GroupParams.GROUP_FIELD, "uid");
        solrquery.set(GroupParams.GROUP_FORMAT, "grouped");
        solrquery.set(GroupParams.GROUP, true);
        solrquery.set(GroupParams.GROUP_OFFSET, 0);
        solrquery.set(GroupParams.GROUP_LIMIT, 100);
        if(searchWordsVo.getRecommendType()==RecommendType.BUSINES_CIRCLE){
            solrquery.set(GroupParams.GROUP_SORT, "verified_by_zzk desc, score desc, div(score_f, map(geodist(latlng_p,25.022167,121.528242),0,1,1)) desc, changed desc");
        }else{
            solrquery.set(GroupParams.GROUP_SORT, "verified_by_zzk desc, score desc, score_f desc, changed desc");
        }
        /*
         * fl
         */
        solrquery.set(CommonParams.FL, "*, distance:geodist(latlng_p, 25.022167, 121.528242)");
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
