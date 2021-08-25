package com.kenProject.eduService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenProject.Servicebase.exceptionHandler.KenProjectException;
import com.kenProject.commonutils.Result;
import com.kenProject.eduService.entity.EudTeacher;
import com.kenProject.eduService.entity.vo.TeacherQuery;
import com.kenProject.eduService.service.EudTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-20
 */

@Api(tags="讲师管理")
@RestController
@RequestMapping("/eduService/edu_teacher")
@CrossOrigin
public class EudTeacherController {

    //访问地址：http://localhost:8000/eduService/eud-teacher/findAll

    //把service注入
    @Autowired
    private EudTeacherService teacherService;

    //1 查询讲师表中所有的数据
    //rest风格
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("/findAll")
    public Result findAll() {
        //调用Service的方法
        List<EudTeacher> teachers = teacherService.list(null);
        return Result.ok().data("items", teachers);
    }

    //2.逻辑删除讲师的
    @ApiOperation(value = "逻辑删除讲师根据ID号")
    @DeleteMapping("{id}")
    public Result removeTeacher(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id) {
        boolean flag = teacherService.removeById(id);
        if (flag == true) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    //3、分页查询讲师的方法
    @GetMapping("/pageTeacher/{current}/{limit}")
    public Result pageListTeacher(@PathVariable long current, @PathVariable long limit) {
        //1 创建配置对象
        //传入当前页和每页记录数
        Page<EudTeacher> eudTeacherPage = new Page<>(current, limit);
        //调用方法实现分离
        //调用方法的时候，底层封装，把所有数据封装到eudTeacherPage对象里去
        teacherService.page(eudTeacherPage, null);

        try {
            int i=10/0;
        } catch (Exception e) {
            throw new KenProjectException(2000,"执行了自定义异常");
        }
        long total = eudTeacherPage.getTotal();
        List<EudTeacher> records = eudTeacherPage.getRecords();

        return Result.ok().data("total", total).data("records", records);
    }

    //4、条件查询带分页的方法
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public Result pageTeacherCondition(@PathVariable long current, @PathVariable long limit,
                                       @RequestBody(required = false) TeacherQuery teacherQuery) {
        //创建page对象
        Page<EudTeacher> pageTeacher = new Page<>(current, limit);
        //构建条件
        QueryWrapper<EudTeacher> wrapper = new QueryWrapper<>();
        //构建条件
        //mybatis学过 动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，不为空拼接连接
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        //排序
        wrapper.orderByDesc("gmt_create");

        //调用方法实现条件查询
        teacherService.page(pageTeacher, wrapper);
        long total = pageTeacher.getTotal();
        List<EudTeacher> records = pageTeacher.getRecords();
        return Result.ok().data("total", total).data("records", records);
    }

    //添加讲师接口的方法
    @PostMapping("/addTeacher")
    public Result addTeacher(@RequestBody(required = false)EudTeacher eudTeacher){
        boolean save=teacherService.save(eudTeacher);
        return save?Result.ok():Result.error();
    }


    //根据讲师id进行查询
    @GetMapping("/getTeacher/{id}")
    public Result getTeacher(@PathVariable String id){
        EudTeacher teacher = teacherService.getById(id);
        return Result.ok().data("teacher",teacher);
    }

    //修改讲师
    @PostMapping("updateTeacher")
    public Result update(@RequestBody EudTeacher eudTeacher){
        boolean flag=teacherService.updateById(eudTeacher);
        return  flag?Result.ok():Result.error();
    }

    //根据id修改
    @PutMapping("{id}")
    public Result updateById(@PathVariable String id
            ,@RequestBody(required = false) EudTeacher eudTeacher){
        eudTeacher.setId(id);
        teacherService.updateById(eudTeacher);
        return Result.ok();
    }
}

