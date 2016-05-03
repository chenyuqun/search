/**  
 * Project Name:recommend-service  <br/>
 * File Name:BNBServiceTypeHandler.java  <br/>
 * Package Name:com.zizaike.recommend.dao.handler  <br/>
 * Date:2015年11月4日下午9:49:43  <br/>
 * Copyright (c) 2015, zizaike.com All Rights Reserved.  
 *  
*/  
  
package com.zizaike.recommend.dao.handler;  

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.zizaike.entity.solr.BNBServiceType;

/**  
 * ClassName:BNBServiceTypeHandler <br/>  
 * Function: 推荐类型handler. <br/>  
 * Reason:  handler. <br/>  
 * Date:     2015年11月4日 下午9:49:43 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public class BNBServiceTypeHandler extends BaseTypeHandler<BNBServiceType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BNBServiceType parameter,
        JdbcType jdbcType) throws SQLException {

      ps.setString(i, parameter.getValue());

    }

    @Override
    public BNBServiceType getNullableResult(ResultSet rs, String columnName) throws SQLException {

      String value = rs.getString(columnName);
      return BNBServiceType.findByValue(value);
    }

    @Override
    public BNBServiceType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

      String value = rs.getString(columnIndex);
      return BNBServiceType.findByValue(value);
    }

    @Override
    public BNBServiceType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

      String value = cs.getString(columnIndex);
      return BNBServiceType.findByValue(value);
    }

}
  
