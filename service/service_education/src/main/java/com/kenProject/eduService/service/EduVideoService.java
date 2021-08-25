package com.kenProject.eduService.service;

import com.kenProject.eduService.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-08-05
 */
public interface EduVideoService extends IService<EduVideo> {
    //1.根据课程id删除小节
    void removeVideoByCourseId(String courseId);

}
