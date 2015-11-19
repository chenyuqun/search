/**  
 * Project Name:recommend-service  <br/>
 * File Name:RecommendAreaServiceImpl.java  <br/>
 * Package Name:com.zizaike.recommend.service.impl  <br/>
 * Date:2015年11月19日下午3:37:15  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
 */

package com.zizaike.recommend.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.DestConfig;
import com.zizaike.entity.recommend.Loctype;
import com.zizaike.entity.recommend.Recommend;
import com.zizaike.entity.recommend.vo.CountryArea;
import com.zizaike.entity.recommend.vo.RecommendArea;
import com.zizaike.is.recommend.RecommendAreaService;
import com.zizaike.recommend.dao.DestConfigDao;
import com.zizaike.recommend.dao.HotRecommendDao;
import com.zizaike.recommend.dao.LoctypeDao;

/**
 * ClassName:RecommendAreaServiceImpl <br/>
 * Function: 推荐+行政(省市). <br/>
 * Date: 2015年11月19日 下午3:37:15 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.7
 * @see
 */
@Service
public class RecommendAreaServiceImpl implements RecommendAreaService {
    @Autowired
    private DestConfigDao destConfigDao;
    @Autowired
    private HotRecommendDao hotRecommendDao;
    @Autowired
    private LoctypeDao loctypeDao;

    @Override
    public RecommendArea query() throws ZZKServiceException {
        List<Recommend> recommends = hotRecommendDao.quryHotRecommend();
        List<DestConfig> countrys =    destConfigDao.query();
        List<Loctype> loctypes = loctypeDao.queryByAreaLevel();
        List<CountryArea> countryAreas = new ArrayList<CountryArea>();
        Map<Integer,List<Loctype>> map = new HashMap<Integer,List<Loctype>>();
        List<Loctype> areas = null;
        for (Loctype loctype : loctypes) {
            if(map.get(loctype.getDestId()) == null){
                areas = new ArrayList<Loctype>();
            }else{
                areas = map.get(loctype.getDestId());
            }
            areas.add(loctype);
            map.put(loctype.getDestId(), areas);
        }
        RecommendArea recommendArea = new RecommendArea();
        recommendArea.setRecommends(recommends);
        for(DestConfig country : countrys){
            CountryArea countryArea = new CountryArea();
            countryArea.setCountry(country);
            countryArea.setAreaList(map.get(country.getDestId()));
            countryAreas.add(countryArea);
        }
        recommendArea.setCountryAreas(countryAreas);
        return recommendArea;
    }

}
