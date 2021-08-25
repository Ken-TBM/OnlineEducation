package com.kenProject.eduService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenProject.commonutils.Result;
import com.kenProject.eduService.entity.EduCourse;
import com.kenProject.eduService.entity.vo.CourseInfoVo;
import com.kenProject.eduService.entity.vo.CoursePublicVo;
import com.kenProject.eduService.entity.vo.CourseQuery;
import com.kenProject.eduService.service.EduCourseService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-05
 */
@RestController
@RequestMapping("/eduService/edu-course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService eduCourseService;

    //课程列表
    //TODO 完善条件查询带分页
    @PostMapping("pageCondition/{current}/{limit}")
    public Result PageCourseCondition(@PathVariable long current, @PathVariable long limit,
                                      @RequestBody(required = false)CourseQuery courseQuery){
        //创建page对象
        Page<EduCourse> pageCourse = new Page<>(current,limit);
        //构建查询条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        //判断条件值是否为空
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        //排序
        wrapper.orderByDesc("gmt_create");

        eduCourseService.page(pageCourse,wrapper);
        long total = pageCourse.getTotal();
        List<EduCourse> records = pageCourse.getRecords();
        return Result.ok().data("total",total).data("records",records);
    }

    @GetMapping
    public Result getCourseList(){
        List<EduCourse> list = eduCourseService.list(null);
        return Result.ok().data("list",list);
    }



    private static final String STATUS="Normal";
    //添加课程基本信息的方法
    @PostMapping("addCourseInfo")
    public Result addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        //返回添加之后课程id，为了后面添加大纲使用
        String id=eduCourseService.saveCourseInfo(courseInfoVo);
        return Result.ok().data("courseId",id);
    }


    //根据课程查询课程基本信息
    @GetMapping("getCourse/{courseId}")
    public Result getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo =eduCourseService.getCourseInfo(courseId);
        System.out.println("查询方法结束");
        return Result.ok().data("courseInfoVo",courseInfoVo);
    }

    //修改课程信息
    @PostMapping("updateCourseInfo")
    public Result updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        eduCourseService.updateCourseInfoVo(courseInfoVo);
        System.out.println("执行了修改信息");
        return Result.ok();
    }


    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public Result getPublishCourseInfo(@PathVariable String id){
        CoursePublicVo coursePublicVo=eduCourseService.publishCourseInfo(id);
        return Result.ok().data("publishCourse",coursePublicVo);
    }

    //课程最终发布
    //修改课程状态
    @PostMapping("publishCourse/{id}")
    public Result publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        //设置课程发布状态
        eduCourse.setStatus(STATUS);
        eduCourseService.updateById(eduCourse);
        return Result.ok();
    }

    //删除课程
    @DeleteMapping("{courseId}")
    public Result deleteCourse(@PathVariable String courseId){
        System.out.println("进来了");
        eduCourseService.removeCourse(courseId);
        return Result.ok();
    }
}

