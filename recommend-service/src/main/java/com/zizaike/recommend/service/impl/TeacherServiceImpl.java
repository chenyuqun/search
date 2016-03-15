package com.zizaike.recommend.service.impl;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.solr.TeacherShare;
import com.zizaike.recommend.dao.TeacherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Project Name: code <br/>
 * Function:TeacherServiceImpl. <br/>
 * date: 2016/3/1419:42 <br/>
 *
 * @author alex
 * @since JDK 1.7
 */
@Service
public class TeacherServiceImpl implements TeacherService{
    @Autowired
    private TeacherDao teacherDao;
    @Override
    public List<Integer> query() throws ZZKServiceException {
        return teacherDao.query();
    }

}
