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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ServiceException;
import com.zizaike.entity.solr.Room;
import com.zizaike.entity.solr.model.SolrSearchableRoomFields;
import com.zizaike.is.solr.RoomSolrService;
import com.zizaike.solr.place.impl.PlaceSolrServiceImpl;

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
    public List<Room> queryRoomByWords(String words,int locTypeid) throws ServiceException {
        long start = System.currentTimeMillis();
        if (words == null) {
            throw new IllegalParamterException("words is null");
        }
        ArrayList<Room> room=new ArrayList<Room>();
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
}
  
