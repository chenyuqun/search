/**  
 * Project Name:solr-service  <br/>
 * File Name:PlaceTest.java  <br/>
 * Package Name:com.zizaike.solr.user  <br/>
 * Date:2015年11月4日上午10:56:40  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.solr.user;  

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.solr.Place;
import com.zizaike.entity.solr.dto.AssociateWordsDTO;
import com.zizaike.entity.solr.dto.PlaceDTO;
import com.zizaike.is.solr.PlaceSolrService;
import com.zizaike.solr.example.test.AbstractSolrIntegrationTest;

/**  
 * ClassName: PlaceTest <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2015年11月4日 上午10:56:40 <br/>  
 *  
 * @author zzk  
 * @version   
 * @since JDK 1.7  
 */
public class PlaceTest extends AbstractSolrIntegrationTest{

    @Autowired
    PlaceSolrService placeService;
    @Test
    public void testQueryPlaceByWords() throws ZZKServiceException {
       List<Place> place =  placeService.queryPlaceByWords("逢甲");
       Assert.assertNotNull(place, "place is null");
    }
    
    @Test
    public void testQueryPlaceByWordsAndLoc() throws ZZKServiceException {
       List<AssociateWordsDTO> place =  placeService.queryPlaceByWordsAndLoc("淡水",10,60515);
       Assert.assertNotNull(place, "place is null");
    }
    @Test
    public void queryPlaceByLocId() throws ZZKServiceException {
        List<PlaceDTO> place =  placeService.queryPlaceByLocId(2684);
        Assert.assertNotEquals(place.size(), 0);
    }
}
  
