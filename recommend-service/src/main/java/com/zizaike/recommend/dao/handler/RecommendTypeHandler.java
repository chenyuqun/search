/**  
 * Project Name:recommend-service  <br/>
 * File Name:RecommendTypeHandler.java  <br/>
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

import com.zizaike.entity.recommend.RecommendType;

/**  
 * ClassName:RecommendTypeHandler <br/>  
 * Function: 推荐类型handler. <br/>  
 * Reason:  handler. <br/>  
 * Date:     2015年11月4日 下午9:49:43 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public class RecommendTypeHandler extends BaseTypeHandler<RecommendType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, RecommendType parameter,
        JdbcType jdbcType) throws SQLException {

      ps.setInt(i, parameter.getValue());

    }

    @Override
    public RecommendType getNullableResult(ResultSet rs, String columnName) throws SQLException {

      int value = rs.getInt(columnName);
      return RecommendType.findByValue(value);
    }

    @Override
    public RecommendType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

      int value = rs.getInt(columnIndex);
      return RecommendType.findByValue(value);
    }

    @Override
    public RecommendType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

      int value = cs.getInt(columnIndex);
      return RecommendType.findByValue(value);
    }

}
  
