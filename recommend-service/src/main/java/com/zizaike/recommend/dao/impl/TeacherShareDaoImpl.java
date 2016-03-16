package com.zizaike.recommend.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.core.framework.mybatis.impl.GenericMyIbatisDao;
import com.zizaike.entity.recommend.TeacherShare;
import com.zizaike.recommend.dao.TeacherShareDao;

/**
 * Project Name: code <br/>
 * Function:TeacherDaoImpl. <br/>
 * date: 2016/3/1419:52 <br/>
 *
 * @author alex
 * @since JDK 1.7
 */
@Repository
public class TeacherShareDaoImpl extends GenericMyIbatisDao<TeacherShare, Integer>  implements TeacherShareDao {
    /**
     * 命名空间
     */
    private static final String NAMESPACE = "com.zizaike.recommend.dao.TeacherShareMapper." ;

    @Override
    public List<TeacherShare> query() throws ZZKServiceException {

        return this.getSqlSession().selectList(NAMESPACE+"query");
    }
}
