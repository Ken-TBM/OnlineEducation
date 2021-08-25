package com.kenProject.eduService.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenProject.eduService.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenProject.eduService.entity.front.CourseFrontVo;
import com.kenProject.eduService.entity.front.CourseWebVo;
import com.kenProject.eduService.entity.vo.CourseInfoVo;
import com.kenProject.eduService.entity.vo.CoursePublicVo;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-08-05
 */
public interface EduCourseService extends IService<EduCourse> {
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfoVo(CourseInfoVo courseInfoVo);
    //根据课程id查询课程确认信息
    CoursePublicVo publishCourseInfo(String id);

    //删除课程
    void removeCourse(String CourseId);

    //条件查询带分页查询课程 前台
    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);

    //根据课程id，编写sql语句查询课程信息
    CourseWebVo getBaseInfo(String courseId);
}
