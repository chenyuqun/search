/**  
 * Project Name:solr-service  <br/>
 * File Name:PlaceTest.java  <br/>
 * Package Name:com.zizaike.solr.user  <br/>
 * Date:2015年11月6日上午10:56:40  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
 */

package com.zizaike.solr.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.RecommendType;
import com.zizaike.entity.solr.Room;
import com.zizaike.entity.solr.RoomSolr;
import com.zizaike.entity.solr.SearchType;
import com.zizaike.entity.solr.SearchWordsVo;
import com.zizaike.is.solr.RoomSolrService;
import com.zizaike.solr.example.test.AbstractSolrIntegrationTest;

/**
 * ClassName: PlaceTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2015年11月6日 上午10:56:40 <br/>
 * 
 * @author alex
 * @version
 * @since JDK 1.7
 */
public class RoomTest extends AbstractSolrIntegrationTest {

    @Autowired
    RoomSolrService roomService;

//    @Test
//    public void testQueryRoomByWords() throws ZZKServiceException {
//        List<Room> room = roomService.queryRoomByWords("西门町", 2685);
//        Assert.assertNotNull(room, "room is null");
//    }
    
    @Test
    public void testSearchSolr() throws ZZKServiceException {
        SearchWordsVo sv=new SearchWordsVo();
        sv.setDestId(10);
        //sv.setKeyWords("");
        sv.setPage(1);
        sv.setSearchid(0);
        sv.setCheckInDate("");
        sv.setCheckOutDate("");
        sv.setSearchType(SearchType.CITY);
        sv.setOrder(1);
        sv.setService("{\"speed_room\": 0,\"tv\": 0,\"jiesong\": 0,\"baoche\": 0,\"breakfast\": 0,\"aircondition\": 0,\"wifi\": 0,\"park\": 0,\"freezer\": 0,\"toliet\": 0}");
        sv.setMultiprice(11);
        RoomSolr room = roomService.searchSolr(sv);
        Assert.assertNotNull(room, "room is null");
    }
}
