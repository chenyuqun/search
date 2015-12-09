package com.zizaike.recommend.dao.handler;  

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.zizaike.entity.recommend.StatisticsType;

/**  
 * ClassName:StatisticsTypeHandler <br/>  
 * Function: 统计类型handler. <br/>  
 * Reason:  handler. <br/>  
 * Date:     2015年11月4日 下午9:49:43 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public class StatisticsTypeHandler extends BaseTypeHandler<StatisticsType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, StatisticsType parameter,
        JdbcType jdbcType) throws SQLException {

      ps.setInt(i, parameter.getValue());

    }

    @Override
    public StatisticsType getNullableResult(ResultSet rs, String columnName) throws SQLException {

      int value = rs.getInt(columnName);
      return StatisticsType.findByValue(value);
    }

    @Override
    public StatisticsType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

      int value = rs.getInt(columnIndex);
      return StatisticsType.findByValue(value);
    }

    @Override
    public StatisticsType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

      int value = cs.getInt(columnIndex);
      return StatisticsType.findByValue(value);
    }

}
  
