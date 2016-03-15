package com.zizaike.recommend.dao.impl;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.core.framework.mybatis.impl.GenericMyIbatisDao;
import com.zizaike.entity.recommend.DestConfig;
import com.zizaike.entity.solr.TeacherShare;
import com.zizaike.recommend.dao.TeacherDao;

import java.util.List;

/**
 * Project Name: code <br/>
 * Function:TeacherDaoImpl. <br/>
 * date: 2016/3/1419:52 <br/>
 *
 * @author alex
 * @since JDK 1.7
 */
public class TeacherDaoImpl extends GenericMyIbatisDao<TeacherShare, Integer>  implements TeacherDao {
    /**
     * 命名空间
     */
    private static final String NAMESPACE = "com.zizaike.entity.recommend.dao.TeacherShareMapper." ;

    @Override
    public List<Integer> query() throws ZZKServiceException {

        return this.getSqlSession().selectList(NAMESPACE+"query");
    }
}
