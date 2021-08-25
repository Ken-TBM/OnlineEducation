package com.kenProject.eduService.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenProject.commonutils.Result;
import com.kenProject.eduService.entity.EduCourse;
import com.kenProject.eduService.entity.EudTeacher;
import com.kenProject.eduService.service.EduCourseService;
import com.kenProject.eduService.service.EudTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduService/teacherFront")
@CrossOrigin
public class TeacherFrontController {
    @Autowired
    private EudTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    //1、分页查询讲师的方法
    @PostMapping("getTeacherFrontList/{page}/{limit}")
    public Result getTeacherFrontList(@PathVariable long page,@PathVariable long limit){
        Page<EudTeacher> pageTeacher=new Page<>(page,limit);
        Map<String,Object> map=teacherService.getTeacherFrontList(pageTeacher);
        //返回分页中所有的数据
        return Result.ok().data(map);
    }

    //2 讲师详情功能
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public Result getTeacherFrontInfo(@PathVariable String teacherId){
        //1、根据讲师id查询讲师基本信息
        EudTeacher eduTeacher = teacherService.getById(teacherId);
        //2 根据讲师id查询所讲课程
        QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = courseService.list(wrapper);
        return Result.ok().data("teacher",eduTeacher).data("courseList",courseList);
    }

}
