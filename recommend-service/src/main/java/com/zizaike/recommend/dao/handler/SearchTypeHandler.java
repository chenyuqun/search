package com.zizaike.recommend.dao.handler;  

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.zizaike.entity.recommend.SearchType;

/**  
 * ClassName:SearchTypeHandler <br/>  
 * Function: 查询类型handler. <br/>  
 * Reason:  handler. <br/>  
 * Date:     2015年11月4日 下午9:49:43 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public class SearchTypeHandler extends BaseTypeHandler<SearchType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, SearchType parameter,
        JdbcType jdbcType) throws SQLException {

      ps.setInt(i, parameter.getValue());

    }

    @Override
    public SearchType getNullableResult(ResultSet rs, String columnName) throws SQLException {

      int value = rs.getInt(columnName);
      return SearchType.findByValue(value);
    }

    @Override
    public SearchType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

      int value = rs.getInt(columnIndex);
      return SearchType.findByValue(value);
    }

    @Override
    public SearchType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

      int value = cs.getInt(columnIndex);
      return SearchType.findByValue(value);
    }

}
  
