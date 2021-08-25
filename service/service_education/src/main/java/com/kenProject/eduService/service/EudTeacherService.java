package com.kenProject.eduService.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenProject.eduService.entity.EudTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-07-20
 */
public interface EudTeacherService extends IService<EudTeacher> {

    //1 分页查询讲师的方法
    Map<String, Object> getTeacherFrontList(Page<EudTeacher> pageTeacher);
}
