/**  
 * Project Name:solr-service  <br/>
 * File Name:RoomSolrServiceImpl.java  <br/>
 * Package Name:com.zizaike.solr.room.impl  <br/>
 * Date:2015年11月6日下午2:16:08  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.solr.room.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.zizaike.is.common.HanLPService;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.DestConfig;
import com.zizaike.entity.recommend.SearchStatistics;
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
import com.zizaike.solr.domain.event.HotSearchApplicationEvent;
import com.zizaike.solr.domain.event.ResultLessSearchApplicationEvent;
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
    @Autowired
    private HanLPService hanLPService;
    @Autowired
    ApplicationContext applicationContext;
    //图片地址
    private static final String IMAGE_HOST = "http://img1.zzkcdn.com";
    private static final Integer pageSize=10;
    
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
        SearchStatistics searchStatistics = new SearchStatistics();
        searchStatistics.setChannel(searchWordsVo.getChannel());
        searchStatistics.setKeyWords(searchWordsVo.getKeyWords());
        /**
         * search 1普通查询 地点关联 2 poi查询 坐标关联
         */
        int searchType=1;
        if(searchWordsVo.getSearchType()==SearchType.BUSINES_CIRCLE
                ||searchWordsVo.getSearchType()==SearchType.BUSINESS_AREA
                ||searchWordsVo.getSearchType()==SearchType.SCENIC_SPOTS
                ||searchWordsVo.getSearchType()==SearchType.SPORTVAN
                ){
            searchStatistics.setPoiId(searchWordsVo.getSearchid());
            searchType=2;
        }else{
            searchStatistics.setLocId(searchWordsVo.getSearchid());
        }
        //发布热搜统计
        applicationContext.publishEvent(new HotSearchApplicationEvent(searchStatistics));
       
        /**
         * 获取坐标信息
         */
        String geoSort="";
        String geoFq="";
        String geoFl="";
        //促销  1为促销  优惠,促销,打折 只要有一个就可以显示
        Integer promotion=0 ;
        if(searchType==2){
            Place place=placeSolrService.queryPlaceById(searchWordsVo.getSearchid());
            geoSort="div(score_f, map(geodist(latlng_p,"+place.getGoogleMapLat()+","+place.getGoogleMapLng()+"),0,1,1))";
            geoFq="{!geofilt pt="
                    +place.getGoogleMapLat()+","+place.getGoogleMapLng()
                    +" sfield=latlng_p d="
                    /**
                     * 如果搜索半径不为空的话则用搜索半径,如果为空则用poi默认的搜索半径
                     */
                    + (searchWordsVo.getSearchRadius() != null ? searchWordsVo.getSearchRadius(): place.getSearchRadius())
                    +"}";
            geoFl="*, distance:geodist(latlng_p, "+place.getGoogleMapLat()+","+place.getGoogleMapLng()+")";
        }
        if(searchWordsVo.getKeyWords()==""||searchWordsVo.getKeyWords()==null||searchWordsVo.getKeyWords().isEmpty()){
            searchWordsVo.setKeyWords("*:*");
        }
        SolrQuery solrquery=new SolrQuery();
        //把繁体转成简体再查询
        solrquery.set(CommonParams.Q, hanLPService.convertToSimplifiedChinese(searchWordsVo.getKeyWords()));
        solrquery.set(CommonParams.WT,"json");
        solrquery.set("q.op","OR");
        /*
         * DisMax Conditions
         */
        solrquery.set(DisMaxParams.MM, "1");
        solrquery.set(DisMaxParams.PF, "username^2000 user_address^2000");
        solrquery.set(DisMaxParams.QF, "username^20 user_address^20");
        solrquery.set("defType","edismax");
        /*
         * Sort Conditions
         */
        solrquery.setSort("verified_by_zzk", ORDER.desc);
        if(searchWordsVo.getOrder()==1||searchWordsVo.getOrder()==0){
            if(searchWordsVo.getKeyWords().equals("*:*")){
                /*
                 * 默认排序
                 */
                solrquery.addSort("speed_room",ORDER.desc);
            }
            solrquery.addSort("score", ORDER.desc);
        }else if(searchWordsVo.getOrder()==2){
            /*
             * 价格降序 此处有坑
             */
            solrquery.addSort("int_price", ORDER.asc);
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
        }else if(searchWordsVo.getOrder()==5){
            solrquery.addSort("distance", ORDER.asc);
        }else if(searchWordsVo.getOrder()==6){
            solrquery.addSort("distance", ORDER.desc);
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
        solrquery.setStart((searchWordsVo.getPage()-1)*pageSize);
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
        if(searchWordsVo.getPrice()!=null&&searchWordsVo.getPrice()!=""&&searchWordsVo.getPrice().isEmpty()==false){
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
        if(searchWordsVo.getService()!=null&&searchWordsVo.getService()!=""&&searchWordsVo.getService().isEmpty()==false){
          HashMap map=JSON.parseObject(searchWordsVo.getService(), new TypeReference<HashMap<String,String>>(){});
          /*
           * /速订
           */
          if(map.get("speed_room")!=null&&map.get("speed_room")!=""&&map.get("speed_room").equals("1")){
              filterQueries.append(" AND speed_room:1");
          }
          /*
           * 接送服务
           */
          if(map.get("jiesong")!=null&&map.get("jiesong")!=""&&map.get("jiesong").equals("1")){
              filterQueries.append(" AND jiesong_service_i:1");
          }
          /*
           * 包车服务
           */
          if(map.get("baoche")!=null&&map.get("baoche")!=""&&map.get("baoche").equals("1")){
              filterQueries.append(" AND baoche_service_i:1");
          }
          /*
           * 早餐服务
           */
          if(map.get("breakfast")!=null&&map.get("breakfast")!=""&&map.get("breakfast").equals("1")){
              filterQueries.append(" AND breakfast:1");
          }
          /*
           * 独立卫生间
           */
          if(map.get("toliet")!=null&&map.get("toliet")!=""&&map.get("toliet").equals("1")){
              filterQueries.append(" AND roomsetting:独立卫浴");
          }
          /*
           * 电视
           */
          if(map.get("tv")!=null&&map.get("tv")!=""&&map.get("tv").equals("1")){
              filterQueries.append(" AND roomsetting:电视机");
          }
          /*
           * 空调
           */
          if(map.get("aircondition")!=null&&map.get("aircondition")!=""&&map.get("aircondition").equals("1")){
              filterQueries.append(" AND roomsetting:空调");
          }
          /*
           * 冰箱
           */
          if(map.get("freezer")!=null&&map.get("freezer")!=""&&map.get("freezer").equals("1")){
              filterQueries.append(" AND roomsetting:电冰箱");
          }
          /*
           * wifi
           */
          if(map.get("wifi")!=null&&map.get("wifi")!=""&&map.get("wifi").equals("1")){
              filterQueries.append(" AND wifi_i:1");
          }
          /*
           * 免费停车
           */
          if(map.get("park")!=null&&map.get("park")!=""&&map.get("park").equals("1")){
              filterQueries.append(" AND roomsetting:免费停车位");
          }
          /*
           * 是否促销
           */
          if(map.get("promotion")!=null&&map.get("promotion")!=""&&map.get("promotion").equals("1")){
              promotion = 1;
          }
          
        }
        List<String> dataList = new ArrayList<String>();
        /*
         * 日期
         */
        if(searchWordsVo.getCheckInDate()!=null&&searchWordsVo.getCheckOutDate()!=null
            &&searchWordsVo.getCheckInDate()!=""&&searchWordsVo.getCheckOutDate()!=""
            &&searchWordsVo.getCheckInDate().isEmpty()==false&&searchWordsVo.getCheckOutDate().isEmpty()==false){
            StringBuffer sb=new StringBuffer();
            StringBuffer promotionSB=new StringBuffer();
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
                promotionSB.append(new SimpleDateFormat("MMdd").format(date1));
                dataList.add(new SimpleDateFormat("MMdd").format(date1));
                if(s>1){
                    int i=1;
                    do{
                        long todayDate = date1.getTime() + i * 24 * 60 * 60 * 1000;
                        Date tmDate = new Date(todayDate);
                        sb.append(" OR "+new SimpleDateFormat("MMdd").format(tmDate));
                        promotionSB.append(" AND "+new SimpleDateFormat("MMdd").format(tmDate));
                        dataList.add(new SimpleDateFormat("MMdd").format(tmDate));
                        i++;
                    }while(i<s);
                        
                  }
            }catch (Exception e) {
                  System.out.println("转换错误");
           }
           
            filterQueries.append(" AND (*:* AND NOT soldout_room_dates_ss:("+sb+"))");
            if(promotion==1){
                filterQueries.append(" AND (discount_room_dates_ss:("+promotionSB+") OR is_bnb_cuxiao_i:1 OR is_bnb_first_order_i:1)") ;
            }
        }else{
            if(promotion==1){
                filterQueries.append(" AND ( is_bnb_cuxiao_i:1 OR is_bnb_first_order_i:1 )") ;
            }
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
             * 价格降序 此处有坑
             */
            groupSort.append(", int_price asc, ");
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
            if(searchWordsVo.getOrder()==2){
               /*
                * 需要获得全部页码
                */
                QueryResponse qr=getSolrOperations().getSolrServer().query(solrquery);
                GroupResponse gr=qr.getGroupResponse();
                //匹配民宿数目
                int uids=gr.getValues().get(0).getNGroups();
                if((uids-(searchWordsVo.getPage())*pageSize)>0){
                    solrquery.setStart(uids-(searchWordsVo.getPage())*pageSize);
                }else{
                    solrquery.setStart(0);
                    solrquery.setRows((uids-(searchWordsVo.getPage())*pageSize)+pageSize);
                }
            }
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
            if(searchWordsVo.getOrder()==2){
                Collections.reverse(lg);
            }
            List<RoomList> lsRoomList=new ArrayList<RoomList>();
            //无结果统计
            if(matches == 0){
                applicationContext.publishEvent(new ResultLessSearchApplicationEvent(searchStatistics));
            }
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
                    //促销日期
                    List<String> discountRoomDataList = lr.get(0).getDiscountRoomDatesSs();
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
                    //增加促销字段
                    roomList.setIsBnbCuxiaoI(lr.get(0).getBnbCuxiaoI());
                    roomList.setIsBnbFirstOrderI(lr.get(0).getBnbFirstOrderI());
                    String promotionInfo = null;
                    //优惠逻辑
                    if(roomList.getIsBnbCuxiaoI()==true || roomList.getIsPromotion()==1){
                        promotionInfo  = "感恩特惠";
                    }
                    if(roomList.getIsBnbFirstOrderI()==true){
                        promotionInfo  = "首单立减30元";
                    }
                    roomList.setPromotionInfo(promotionInfo);
                    roomList.setCommentNum(commentNum);
                    roomList.setHsRatingAvg(new BigDecimal(hsRatingAvgI/20.00).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
                    
                    roomList.setIsSpeed(isSpeed);
                    roomList.setMinPrice(minPrice);
                    roomList.setUsername(username);
                    if(discountRoomDataList!=null && dataList !=null &&discountRoomDataList.containsAll(dataList)){
                        roomList.setIsPromotion(1);
                    }
                    if(homeStayImage!=null&&homeStayImage.contains("public/zzk_")){
                        roomList.setHomeStayImage(IMAGE_HOST+"/"+homeStayImage.substring(7)+"-homepic800x600.jpg");
                    }else if(homeStayImage!=""){
                        roomList.setHomeStayImage(IMAGE_HOST+"/"+homeStayImage+"/2000x1500.jpg-homepic800x600.jpg");
                    }
                    
                    if(userPhoto!=null&&userPhoto.contains("public/zzk_")){
                        roomList.setUserPhoto(IMAGE_HOST+"/"+userPhoto.substring(7)+"-homepic800x600.jpg");
                    }else if(userPhoto!=""){
                        roomList.setUserPhoto(IMAGE_HOST+"/"+userPhoto+"/2000x1500.jpg-homepic800x600.jpg");
                    }
              
                    if(homeStayImage==""||userPhoto==""){
                        List<String> vec=new ArrayList<String>();
                        vec.add("018c29c499c2e96d3bcfe068fa1ab8f5a32580091062b0-EwF0Fe_fw658.jpg");
                        vec.add("0566ec949b228424a444bbe0f8e9aed794fccff315c2c-l62810_fw658.jpg");        
                        vec.add("0ca422630ee67bf05cd1dd133bdb0428a292db2bb5f98-qn0Zoh_fw658.jpg");      
                        vec.add("122272284471416aa4da900cef91e727f239a0401f060-8kszBW_fw658.jpg");     
                        vec.add("1584005008d83b6d806c014665febafae755dc0937789-cRjjuD_fw658.jpg");
                        vec.add("1628da4955d620df23b4071aff61b2853384715ff3fef-zL2mVm_fw658.jpg");
                        vec.add("2a7c0d4c214e16c86a5e05dfe035aaa72140541216a13-W3NIbQ_fw658.jpg");
                        vec.add("2c7e6652459a8b45bbcd54be02dca209af591be78395-ElsBmS_fw658.jpg");
                        vec.add("2d478d45835c9d6683ba7976563b28b561691a7c13963-DUwhDH_fw658.jpg");
                        vec.add("3b81f236f18e46d7bbf624369ca5465c81bfb348a8c77-dR0b0p_fw658.png");
                        vec.add("3d54504aee3703f10883fa6f2e196b0483f8d6d278316-pCDdPs_fw658.jpg");
                        vec.add("4b20b8303e26647640b5efee5508ef6ae9092687a6fad-ymhOFr_fw658.jpg");
                        vec.add("5eedaf8f86edb1b886745bd2b75536755a898d6abe13-k40YZf_fw658.jpg");
                        vec.add("63d85bb7d738466fc8b4e83d97f3a3a6e1fb47f3798a-bhNK32_fw658.jpg");
                        vec.add("661232e63817e14a4d8c33d950d8e4699f83792441cef-GMs2mk_fw658.jpg");                        
                        vec.add("6dec09e6eb3bee4ff137128fdafd3a49b901c09d1de17e-pjEgNF_fw658.jpg");
                        vec.add("74481ddae7d86c7f64dca1fb9f403207cf237a1414bf5-jmwVIk_fw658.jpg");
                        vec.add("7fdf8cc84f999d15befee42d3e1432554b246fe415685-wj1btk_fw658.jpg");
                        vec.add("b07253f17a0cfb6e9738a7cec715428512dcc1b126ef2-HNpZsq_fw658.jpg");
                        vec.add("b34c28278fdc319111379c17af57a196abdd043c1600a-ZY1NjU_fw658.jpg");
                        vec.add("b40d2c5188c8bfe077d49526c0ad4fe01d97c72823421-i1mqog_fw658.jpg");
                        vec.add("b512076cba296517702852b57365af12d084c6091948b-GLZ0Vv_fw658.jpg");
                        vec.add("b956b639981e7acf31776e673d9bbd7f2dd2ee19b142-Whtf4b_fw658.jpg");                        
                        vec.add("be636586ca40826c5eb7f401c7a562b52e6d7f59877d-n1oqM6_fw658.jpg");   
                        vec.add("cc557d5c5ce6ac70f9412ec5dcddf12743b764152d6c8-6mGAbb_fw658.jpg");   
                        vec.add("d8970fe63def424b882b66e1f9f47ea1e6b02d3b17377-8xWfdo_fw658.jpg");   
                        vec.add("dc0a2569441ed87d99d8398a5c0e21077b3612b44acbb-FulnVQ_fw658.jpg");   
                        vec.add("dc47d13633fcb913ba8632b25c4e445b4466212af1a7-Q6TEQX_fw658.jpg");   
                        vec.add("dcce75ee22b0130e558e2702ef03f06e182f6ad220c9b-4CoV46_fw658.jpg");   
                        vec.add("e642bbd2cf56107dbe67534900bfbc23582865b98c666-sTd3va_fw658.png");   
                        vec.add("e935e547deb6834e0c10c8a0a444f8de00f3f9a912017-lg0BWy_fw658.jpg");   
                        vec.add("ed293676853429978545b3b252d033d249813d6b5ab5a-FUCw3V_fw658.jpg");
                        vec.add("effd6c65820509830efc4a270e516d91b7fffdeae70b0-ACmMl8_fw658.jpg");
                        vec.add("f2470ae283800bd8e66fbd3993fbcdee706656fecf8c-qIIKdn_fw658.jpg");
                        vec.add("f30df96a4de1f210e1a522a8f91c8dbe6eea41eff5b19-SOOcZJ_fw658.jpg");
                        vec.add("f9425ac4ab105c7617126dde6668644ec22b724426a40-Ho97NB_fw658.jpg");
                        vec.add("fc878bb0d8203cce99a02062047156f04a11cec922390-DIenKe_fw658.jpg");
                        vec.add("ffc8be4570331ba6957da29362a1d33ce9c9701b2e3c1-bVKqtS_fw658.jpg"); 
                        int num=vec.size();
                        int key = Integer.parseInt(lg.get(i).getGroupValue())%num;
                        String url = "http://pages.zizaike.com/a/newavatar/";
                        if(homeStayImage=="")
                        roomList.setHomeStayImage(url+vec.get(key));
                        if(userPhoto=="")
                        roomList.setUserPhoto(url+vec.get(key));
                    }
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
                        //向上取整
                        roomList.setMinPrice((int)Math.ceil(minPriceAct));
                        roomList.setCurrencyCode(currencyCode);
                    }
                    lsRoomList.add(roomList);
                }
            }
            roomSolr.setRoomGroup(lsRoomList);
        } catch (SolrServerException e) {             
            e.printStackTrace();
            LOG.error("when call searchSolr error SolrServerException :{}", e.toString());
        }
        LOG.info("when call searchSolr, use: {}ms", System.currentTimeMillis() - start);
        return roomSolr;
    }
    
    
}     
