package com.kenProject.eduService.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kenProject.commonutils.Result;
import com.kenProject.eduService.entity.EduCourse;
import com.kenProject.eduService.entity.EudTeacher;
import com.kenProject.eduService.service.EduCourseService;
import com.kenProject.eduService.service.EudTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduService/indexFront")
@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EudTeacherService teacherService;

    //显示前8条热门课程，查询前四位名师
   @GetMapping("index")
   public Result index(){
       QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();
       wrapper.orderByDesc("id");
       wrapper.last("limit 8");
       List<EduCourse> courseList = courseService.list(wrapper);

       QueryWrapper<EudTeacher> teacherWrapper=new QueryWrapper<>();
       wrapper.orderByDesc("id");
       wrapper.last("limit 4");
       List<EudTeacher> teacherList = teacherService.list(teacherWrapper);

       return Result.ok().data("courseList",courseList).data("teacherList",teacherList);
   }
}
