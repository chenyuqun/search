package com.zizaike.recommend.service.impl;

import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.entity.recommend.TeacherShare;
import com.zizaike.is.recommend.TeacherShareService;
import com.zizaike.recommend.dao.TeacherShareDao;

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
public class TeacherShareServiceImpl implements TeacherShareService{
    @Autowired
    private TeacherShareDao teacherShareDao;
    @Override
    public List<TeacherShare> query() throws ZZKServiceException {
        return teacherShareDao.query();
    }

}
