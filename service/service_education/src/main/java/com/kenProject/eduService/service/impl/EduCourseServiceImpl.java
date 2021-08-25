package com.kenProject.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenProject.Servicebase.exceptionHandler.KenProjectException;
import com.kenProject.eduService.entity.EduChapter;
import com.kenProject.eduService.entity.EduCourse;
import com.kenProject.eduService.entity.EduCourseDescription;
import com.kenProject.eduService.entity.EudTeacher;
import com.kenProject.eduService.entity.front.CourseFrontVo;
import com.kenProject.eduService.entity.front.CourseWebVo;
import com.kenProject.eduService.entity.vo.CourseInfoVo;
import com.kenProject.eduService.entity.vo.CoursePublicVo;
import com.kenProject.eduService.mapper.EduCourseMapper;
import com.kenProject.eduService.service.EduChapterService;
import com.kenProject.eduService.service.EduCourseDescriptionService;
import com.kenProject.eduService.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenProject.eduService.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-08-05
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    //课程描述注入
    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    //注入小节和章节的service
    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1.向课程表添加课程基本信息
        //CourseInfoVo对象转|换eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);

        if (insert==0){
            throw new KenProjectException(20001,"添加课程信息失败");
        }

        //获取添加添加之后的课程id
        String courseId = eduCourse.getId();


        //2.向课程简介表添加课程简介
        //edu_course_description
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        //设置描述id就是课程id
        eduCourseDescription.setId(courseId);
        eduCourseDescriptionService.save(eduCourseDescription);

        return  courseId;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1 查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        System.out.println(eduCourse);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);


        //2 查询描述表
        EduCourseDescription courseDescription = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());

        return courseInfoVo;
    }

    //修改课程信息
    @Override
    public void updateCourseInfoVo(CourseInfoVo courseInfoVo) {
        // 1 修改课程表
        EduCourse eduCourse=new EduCourse();
        System.out.println(courseInfoVo);
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        System.out.println(eduCourse);
        int update = baseMapper.updateById(eduCourse);
        System.out.println(update);
        if (update==0){
            throw new KenProjectException(20001,"修改课程信息失败");
        }
        // 2 修改描述
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.updateById(description);


    }

    //根据课程id查询课程确认信息
    @Override
    public CoursePublicVo publishCourseInfo(String id) {
        //调用mapper
        CoursePublicVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    @Override
    public void removeCourse(String courseId) {
        //1.根据课程id删除小节
        eduVideoService.removeVideoByCourseId(courseId);
        //2.根据课程id删除章节
        eduChapterService.removeChapterById(courseId);
        //3.根据课程id删除描述
        eduCourseDescriptionService.removeById(courseId);
        //4.根据课程id删除课程本身
        int result = baseMapper.deleteById(courseId);
        if (result ==0){
            throw new KenProjectException(20000,"删除课程失败");
        }
    }

    //条件查询带分页查询课程 前台
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();
        //判断条件值是否为空，不为空拼接
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){
            //一级分类
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())){
            //二级分类
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){
            //关注度
            wrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){
            //最新
            wrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())){
            //价格
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageCourse,wrapper);
        List<EduCourse> records = pageCourse.getRecords();
        long current = pageCourse.getCurrent();
        long pages = pageCourse.getPages();
        long size = pageCourse.getSize();
        long total = pageCourse.getTotal();
        boolean hasNext = pageCourse.hasNext();
        boolean hasPrevious = pageCourse.hasPrevious();

        //把分页数据放到map集合中
        Map<String,Object> map= new HashMap<>();
        map.put("items",records);
        map.put("current",current);
        map.put("pages",pages);
        map.put("size",size);
        map.put("total",total);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);
        return map;
    }

    @Override
    public CourseWebVo getBaseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
