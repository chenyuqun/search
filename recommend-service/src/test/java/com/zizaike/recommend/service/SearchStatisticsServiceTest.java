package com.zizaike.recommend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.base.ChannelType;
import com.zizaike.entity.recommend.SearchStatistics;
import com.zizaike.entity.recommend.SearchType;
import com.zizaike.entity.recommend.StatisticsType;
import com.zizaike.is.recommend.SearchStatisticsService;
import com.zizaike.recommend.basetest.BaseTest;

/**
 * ClassName:TtopsAdaptTest <br/>
 * Date: 2015年3月5日 下午3:51:51 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.6
 * @see
 */
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class SearchStatisticsServiceTest extends BaseTest {
    @Autowired
    private SearchStatisticsService searchStatisticsService;

    @Rollback(false)
    @Test(description = "批量插入")
    public void addBatch() throws ZZKServiceException {
        List<SearchStatistics> list = new ArrayList<SearchStatistics>();
        for (int i = 0; i < 100; i++) {
            SearchStatistics searchStatistics = new SearchStatistics();
            searchStatistics.setChannel(ChannelType.APP);
            searchStatistics.setCount(121212L);
            searchStatistics.setKeyWords("测试");
            searchStatistics.setLocId(12322);
            searchStatistics.setPoiId(123222);
            searchStatistics.setSearchType(SearchType.HOT);
            searchStatistics.setStatisticsType(StatisticsType.ERVERY_DAY);
            list.add(searchStatistics);
        }
        searchStatisticsService.addBatch(list);
    }

    @Rollback(false)
    @Test(description = "删除当天数据")
    public void deleteDay() throws ZZKServiceException {
        SearchStatistics searchStatistics = new SearchStatistics();
        searchStatistics.setChannel(ChannelType.APP);
        searchStatistics.setCount(12L);
        searchStatistics.setKeyWords("测试");
        searchStatistics.setLocId(123);
        searchStatistics.setPoiId(123);
        searchStatistics.setSearchType(SearchType.HOT);
        searchStatistics.setStatisticsType(StatisticsType.ERVERY_DAY);
        searchStatisticsService.deleteDay(searchStatistics);
    }

}
