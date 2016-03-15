package com.zizaike.recommend.dao;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.core.framework.springext.database.Slave;
import com.zizaike.entity.recommend.TeacherShare;

import java.util.List;

/**
 * Project Name: code <br/>
 * Function:TeacherDao. <br/>
 * date: 2016/3/1419:46 <br/>
 *
 * @author alex
 * @since JDK 1.7
 */
public interface TeacherShareDao {
    @Slave
    List<TeacherShare> query() throws ZZKServiceException;
}
