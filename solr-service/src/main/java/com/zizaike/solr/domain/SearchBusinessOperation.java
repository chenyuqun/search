/**  
 * Project Name:solr-service  <br/>
 * File Name:SearchBusinessOperation.java  <br/>
 * Package Name:com.zizaike.solr.domain  <br/>
 * Date:2016年4月22日上午11:02:37  <br/>
 * Copyright (c) 2016, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.solr.domain;  

import com.zizaike.core.framework.event.BusinessOperation;

/**  
 * ClassName:SearchBusinessOperation <br/>  
 * Function: 操作. <br/>  
 * Date:     2016年4月22日 上午11:02:37 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public enum SearchBusinessOperation implements BusinessOperation {

        /***  全局搜索 ***/
        SEARCH("SEARCH"),
        /*** 服务搜索 ***/
        SERVICE_SEARCH("SERVICE_SEARCH"),
       ;

        private String operation;

        public String getOperation() {
            return operation;
        }

        private SearchBusinessOperation(String operation) {
            this.operation = operation;
        }



}
  
