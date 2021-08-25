package com.kenProject.eduService.controller.front;

import com.alibaba.excel.event.Order;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenProject.commonutils.JwtUtils;
import com.kenProject.commonutils.Result;
import com.kenProject.commonutils.ordervo.CourseWebVoOrder;
import com.kenProject.eduService.client.OrdersClient;
import com.kenProject.eduService.entity.EduCourse;
import com.kenProject.eduService.entity.chapterr.ChapterVo;
import com.kenProject.eduService.entity.front.CourseFrontVo;
import com.kenProject.eduService.entity.front.CourseWebVo;
import com.kenProject.eduService.service.EduChapterService;
import com.kenProject.eduService.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private OrdersClient ordersClient;

    //条件查询带分页查询课程
    @PostMapping("getFrontInfo/{page}/{limit}")//@RequestBody(required = false)传入值可以为空
    public Result getFrontInfo(@PathVariable long page, @PathVariable long limit,
                               @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> pageCourse=new Page<>(page,limit);
        Map<String,Object> map=eduCourseService.getCourseFrontList(pageCourse,courseFrontVo);

       return Result.ok().data(map);
    }

    //课程详情方法
    @GetMapping("getFrontCourseInfo/{courseId}")
    public Result getCourseInfo(@PathVariable String courseId, HttpServletRequest request){
        //根据课程id，编写sql语句查询课程信息
        CourseWebVo courseWebVo =eduCourseService.getBaseInfo(courseId);

        //根据课程id查询章节小节部分
        List<ChapterVo> chapterVideoList = eduChapterService.getChapterVideoByCourseId(courseId);

        //根据课程id和用户id查询当前课程是否已经支付过了
        String id = JwtUtils.getMemberIdByJwtToken(request);
        boolean isBuyCourse = ordersClient.isBuyCourse(courseId, id);

        return Result.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",isBuyCourse);
    }

    //根据课程id查询课程信息
    @PostMapping("getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id){
        CourseWebVo webVo = eduCourseService.getBaseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(webVo,courseWebVoOrder);
        return courseWebVoOrder;
    }

}