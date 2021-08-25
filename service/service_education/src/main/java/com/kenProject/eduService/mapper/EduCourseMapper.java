package com.kenProject.eduService.mapper;

import com.kenProject.eduService.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kenProject.eduService.entity.front.CourseWebVo;
import com.kenProject.eduService.entity.vo.CoursePublicVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-08-05
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public CoursePublicVo getPublishCourseInfo(String courseId);

    //根据课程id，编写sql语句查询课程信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
