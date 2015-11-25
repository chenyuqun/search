/**  
 * Project Name:solr-service  <br/>
 * File Name:RoomSolrServiceImpl.java  <br/>
 * Package Name:com.zizaike.solr.room.impl  <br/>
 * Date:2015年11月6日下午2:16:08  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.solr.room.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.DestConfig;
import com.zizaike.entity.solr.Place;
import com.zizaike.entity.solr.Room;
import com.zizaike.entity.solr.RoomList;
import com.zizaike.entity.solr.RoomSolr;
import com.zizaike.entity.solr.SearchType;
import com.zizaike.entity.solr.SearchWordsVo;
import com.zizaike.entity.solr.model.SolrSearchableRoomFields;
import com.zizaike.is.recommend.DestConfigService;
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
    
    @Autowired
    public DestConfigService destConfigService;
    //图片地址
    private static final String IMAGE_HOST = "image.zizaike.com";
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
        if(searchWordsVo.getOrder()==1||searchWordsVo.getOrder()==0){
            /*
             * 默认排序
             */
            solrquery.addSort("score", ORDER.desc);
        }else if(searchWordsVo.getOrder()==2){
            /*
             * 价格降序
             */
            solrquery.addSort("int_price", ORDER.desc);
        }else if(searchWordsVo.getOrder()==3){
            solrquery.addSort("int_price", ORDER.asc);
            /*
             * 价格升序
             */
        }else if(searchWordsVo.getOrder()==4){
            /*
             * 好评优先
             */
            solrquery.addSort("hs_comments_num_i", ORDER.desc);
        }
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
        /*
         * app端一页显示10
         */
        solrquery.setRows(10);
        /*
         * Filter Conditions
         */
        StringBuffer filterQueries=new StringBuffer();
        filterQueries.append("status:1 AND id:[0 TO 200000000] AND verified_by_zzk:1 AND -uid:(66 40080 40793 292734)");
        if(searchWordsVo.getDestId()!=0){
            filterQueries.append(" AND dest_id:"+searchWordsVo.getDestId()+"");
        }
        /*
         * 房型
         */
        if(searchWordsVo.getRoomModel()==1||searchWordsVo.getRoomModel()==2
                ||searchWordsVo.getRoomModel()==3||searchWordsVo.getRoomModel()==4
                ){
            filterQueries.append(" AND room_model:"+searchWordsVo.getRoomModel()+"");
        }else if(searchWordsVo.getRoomModel()==5){
            filterQueries.append(" AND room_model:[5 TO *]");
        }else if(searchWordsVo.getRoomModel()==0){
            /*
             * 默认值
             */
            filterQueries.append(" AND room_model:[0 TO *]");
        }
        /*
         * 价格
         */
        if(searchWordsVo.getPrice()!=null&&searchWordsVo.getPrice()!=""){
            String[] price=searchWordsVo.getPrice().split(",");
            /*
             * 是否需要转换币种
             */
            String MinPrice=price[0];
            String MaxPrice=price[1];
            filterQueries.append(" AND int_price:["+MinPrice+" TO "+MaxPrice+"]");
        }
        /*
         *服务
         */
        if(searchWordsVo.getService()!=null&&searchWordsVo.getService()!=""){
          HashMap map=JSON.parseObject(searchWordsVo.getService(), new TypeReference<HashMap<String,String>>(){});
          /*
           * /速订
           */
          if(map.get("speed_room")!=null&&map.get("speed_room")!=""&&map.get("speed_room")=="1"){
              filterQueries.append(" AND speed_room:1");
          }
          /*
           * 接送服务
           */
          if(map.get("jiesong")!=null&&map.get("jiesong")!=""&&map.get("jiesong")=="1"){
              filterQueries.append(" AND jiesong_service_i:1");
          }
          /*
           * 包车服务
           */
          if(map.get("baoche")!=null&&map.get("baoche")!=""&&map.get("baoche")=="1"){
              filterQueries.append(" AND baoche_service_i:1");
          }
          /*
           * 早餐服务
           */
          if(map.get("breakfast")!=null&&map.get("breakfast")!=""&&map.get("breakfast")=="1"){
              filterQueries.append(" AND breakfast:1");
          }
          /*
           * 独立卫生间
           */
          if(map.get("toliet")!=null&&map.get("toliet")!=""&&map.get("toliet")=="1"){
              filterQueries.append(" AND roomsetting:独立卫浴");
          }
          /*
           * 电视
           */
          if(map.get("tv")!=null&&map.get("tv")!=""&&map.get("tv")=="1"){
              filterQueries.append(" AND roomsetting:电视机");
          }
          /*
           * 空调
           */
          if(map.get("aircondition")!=null&&map.get("aircondition")!=""&&map.get("aircondition")=="1"){
              filterQueries.append(" AND roomsetting:空调");
          }
          /*
           * 冰箱
           */
          if(map.get("freezer")!=null&&map.get("freezer")!=""&&map.get("freezer")=="1"){
              filterQueries.append(" AND roomsetting:电冰箱");
          }
          /*
           * wifi
           */
          if(map.get("wifi")!=null&&map.get("wifi")!=""&&map.get("wifi")=="1"){
              filterQueries.append(" AND wifi_i:1");
          }
          /*
           * 免费停车
           */
          if(map.get("park")!=null&&map.get("park")!=""&&map.get("park")=="1"){
              filterQueries.append(" AND roomsetting:免费停车位");
          }
          
        }
        /*
         * 日期
         */
        if(searchWordsVo.getCheckInDate()!=null&&searchWordsVo.getCheckOutDate()!=null
            &&searchWordsVo.getCheckInDate()!=""&&searchWordsVo.getCheckOutDate()!=""){
            StringBuffer sb=new StringBuffer();
            try {
                Date date1;
                Date date2;  
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                date1 = df.parse(searchWordsVo.getCheckInDate());
                date2 = df.parse(searchWordsVo.getCheckOutDate());
                if(date1.getTime()-date2.getTime()>0){
                    Date tmp=date1;
                    date1=date2;
                    date2=tmp;
                }
                int s = (int) ((date2.getTime() - date1.getTime())/ (24 * 60 * 60 * 1000));             
                sb.append(new SimpleDateFormat("MMdd").format(date1));
                if(s>1){
                    int i=1;
                    do{
                        long todayDate = date1.getTime() + i * 24 * 60 * 60 * 1000;
                        Date tmDate = new Date(todayDate);
                        sb.append(" OR "+new SimpleDateFormat("MMdd").format(tmDate));
                        i++;
                    }while(i<s);
                        
                  }
            }catch (Exception e) {
                  System.out.println("转换错误");
           }
           
            filterQueries.append(" AND (*:* AND NOT soldout_room_dates_ss:("+sb+"))");
        }
        if(searchType==1&&searchWordsVo.getSearchid()!=0){
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
        StringBuffer groupSort=new StringBuffer();
        groupSort.append("verified_by_zzk desc");
        if(searchWordsVo.getOrder()==1||searchWordsVo.getOrder()==0){
            /*
             * 默认排序
             */
            groupSort.append(", score desc, ");
        }else if(searchWordsVo.getOrder()==2){
            /*
             * 价格降序
             */
            groupSort.append(", int_price desc, ");
        }else if(searchWordsVo.getOrder()==3){
            /*
             * 价格升序
             */
            groupSort.append(", int_price asc, ");
        }else if(searchWordsVo.getOrder()==4){
            /*
             * 好评优先
             */
            groupSort.append(", hs_comments_num_i desc, ");
        }  
        if(searchType==2){
            solrquery.set(GroupParams.GROUP_SORT, groupSort+geoSort+" desc, changed desc");
        }else{
            solrquery.set(GroupParams.GROUP_SORT, groupSort+"score_f desc, changed desc");
        }
        /*
         * fl
         */
        if(searchType==2){
        solrquery.set(CommonParams.FL, geoFl);
        }
        
        DocumentObjectBinder binder = new DocumentObjectBinder(); 
        RoomSolr roomSolr=new RoomSolr();
        roomSolr.setMatches(0);
        roomSolr.setNgroups(0);
        try {
            QueryResponse qr=getSolrOperations().getSolrServer().query(solrquery);
            GroupResponse gr=qr.getGroupResponse();
           
            //匹配房间数目
            int matches=gr.getValues().get(0).getMatches();
            roomSolr.setMatches(matches);
            //匹配民宿数目
            int ngroups=gr.getValues().get(0).getNGroups();
            roomSolr.setNgroups(ngroups);
            //内容
            List<Group> lg=gr.getValues().get(0).getValues();
            
            List<RoomList> lsRoomList=new ArrayList<RoomList>();
            if(matches>0){
               
            
                for(int i=0;i<lg.size();i++){

                    RoomList roomList=new RoomList();
                    //民宿uid
                    roomList.setUid(Integer.parseInt(lg.get(i).getGroupValue()));
                    //每个民宿对应的房间信息
                    List<Room> lr=binder.getBeans(Room.class, lg.get(i).getResult());
                    //速订
                    int isSpeed=lr.get(0).getHsSpeedRoomI();
                    //评分
                    int hsRatingAvgI=lr.get(0).getHsRatingAvgI();
                    //评论
                    int commentNum=lr.get(0).getHsCommentsNumI();
                    //民宿图片
                    String homeStayImage=lr.get(0).getHomestayDefaultImageS();
                    //民宿名称
                    String username=lr.get(0).getUsername();
                    //民宿地址
                    String address=lr.get(0).getUserAddress();
                    //用户头像
                    String userPhoto=lr.get(0).getUserPhotoFile();
                    //认证名称
                    String userPoiName=lr.get(0).getUserpoiUserName();
                    //认证id
                    int userPoiId=lr.get(0).getUserpoiId();
                    if(searchType==2){
                        Double distance =lr.get(0).getDistance();
                        roomList.setDistance(distance);
                    }
                    /*
                     *  最小价格 人民币/本币
                     */                   
                    int minPrice=lr.get(0).getIntPrice();
                    int minPriceTW=lr.get(0).getIntPriceTW();
                  
                    for(int j=1;j<lr.size();j++){
                        if(lr.get(j).getIntPrice()<minPrice){
                            minPrice=lr.get(j).getIntPrice();                  
                            }
                        if(lr.get(j).getIntPriceTW()<minPriceTW){
                            minPriceTW=lr.get(j).getIntPriceTW();                  
                            }
                        }
                    
                
                    roomList.setAddress(address);
                    roomList.setCommentNum(commentNum);
                    roomList.setHomeStayImage(IMAGE_HOST+homeStayImage+"/200x200.jpg");
                    roomList.setHsRatingAvgI(hsRatingAvgI);
                    roomList.setIsSpeed(isSpeed);
                    roomList.setMinPrice(minPrice);
                    roomList.setUsername(username);
                    roomList.setUserPhoto(IMAGE_HOST+userPhoto+"/200x200.jpg");
                    roomList.setUserPoiId(userPoiId);
                    roomList.setUserPoiName(userPoiName);
                    /*
                     * 需要转换币种  
                     */
                    if(searchWordsVo.getMultiprice()!=0){
                        DestConfig target=destConfigService.queryByDestId(searchWordsVo.getMultiprice());
                        /*
                         * 民宿原始货币
                         */
                        DestConfig from=destConfigService.queryByDestId(lr.get(0).getDestId());
                        /*
                         * 获取汇率
                         */
                        Double rate1=from.getExchangeRate();
                        Double rate2=target.getExchangeRate();
                        String currencyCode=target.getCurrencyCode();
                        Double minPriceAct=((double)minPriceTW)/rate1*rate2;
                        roomList.setMinPrice(minPriceAct.intValue());
                        roomList.setCurrencyCode(currencyCode);
                    }
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
