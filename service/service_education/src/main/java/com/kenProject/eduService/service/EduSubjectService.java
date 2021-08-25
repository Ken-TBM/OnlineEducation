package com.kenProject.eduService.service;

import com.kenProject.eduService.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenProject.eduService.entity.subject.OneSubject;
import com.kenProject.eduService.listener.SubjectListener;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-08-04
 */
public interface EduSubjectService extends IService<EduSubject> {
    //添加课程分类
    void saveSubject(MultipartFile file, EduSubjectService subjectService);

    //课程分类列表（树形结构）
    List<OneSubject> getAllOneTwoSubject();
}
