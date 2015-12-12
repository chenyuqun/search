package com.zizaike.recommend.dao.handler;  

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.zizaike.entity.base.ChannelType;

/**  
 * ClassName:ChannelTypeHandler <br/>  
 * Function: 来源类型handler. <br/>  
 * Reason:  handler. <br/>  
 * Date:     2015年11月4日 下午9:49:43 <br/>  
 * @author   snow.zhang  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public class ChannelTypeHandler extends BaseTypeHandler<ChannelType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ChannelType parameter,
        JdbcType jdbcType) throws SQLException {

      ps.setInt(i, parameter.getValue());

    }

    @Override
    public ChannelType getNullableResult(ResultSet rs, String columnName) throws SQLException {

      int value = rs.getInt(columnName);
      return ChannelType.findByValue(value);
    }

    @Override
    public ChannelType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

      int value = rs.getInt(columnIndex);
      return ChannelType.findByValue(value);
    }

    @Override
    public ChannelType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

      int value = cs.getInt(columnIndex);
      return ChannelType.findByValue(value);
    }

}
  
