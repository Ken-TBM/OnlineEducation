package com.kenProject.eduService.controller;


import com.kenProject.commonutils.Result;
import com.kenProject.eduService.entity.subject.OneSubject;
import com.kenProject.eduService.listener.SubjectListener;
import com.kenProject.eduService.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/eduService/edu-subject")
@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    //获取上传过来的文件，把文件内容读取出来
    @PostMapping("addSubject")
    public Result addSubject(MultipartFile file){
        //上传过来excel文件
        subjectService.saveSubject(file,subjectService);
        return Result.ok();
    }

    //课程分类列表（树形结构）
    @GetMapping("getAllSubject")
    public Result getAllSubject(){
        //list集合的泛型时一级分类
        List<OneSubject> list=subjectService.getAllOneTwoSubject();
        return Result.ok().data("list",list);
    }


}

