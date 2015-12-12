/**  
 * Project Name:recommend-service  <br/>
 * File Name:SearchStatisticsServiceImpl.java  <br/>
 * Package Name:com.zizaike.recommend.service.impl  <br/>
 * Date:2015年12月8日下午6:27:42  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.service.impl;  

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zizaike.core.framework.exception.IllegalParamterException;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.SearchStatistics;
import com.zizaike.is.recommend.SearchStatisticsService;
import com.zizaike.recommend.dao.SearchStatisticsDao;

/**  
 * ClassName:SearchStatisticsServiceImpl <br/>  
 * Function: 搜索统计服务. <br/>  
 * Date:     2015年12月8日 下午6:27:42 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
@Service
public class SearchStatisticsServiceImpl implements SearchStatisticsService {
    @Autowired
    private SearchStatisticsDao searchStatisticsDao;
    @Override
    public void addBatch(List<SearchStatistics> list) throws ZZKServiceException {
        if(list.size()!=0){
            deleteDay(list.get(0));
            searchStatisticsDao.addBatch(list);
        }
    }
    @Override
    public void deleteDay(SearchStatistics searchStatistics) throws ZZKServiceException {
         if(searchStatistics==null){
             throw new  IllegalParamterException("searchStatistics is null");
         }
         if(searchStatistics.getChannel()==null){
             throw new  IllegalParamterException("searchStatistics  channel is null");
         }
         if(searchStatistics.getSearchType()==null){
             throw new  IllegalParamterException("searchStatistics  searchType is null");
         }
         if(searchStatistics.getStatisticsType()==null){
             throw new  IllegalParamterException("searchStatistics  statisticsType is null");
         }
        searchStatisticsDao.deleteDay(searchStatistics);
    }

}
  
