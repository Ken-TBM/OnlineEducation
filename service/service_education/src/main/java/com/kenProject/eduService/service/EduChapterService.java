package com.kenProject.eduService.service;

import com.kenProject.eduService.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenProject.eduService.entity.chapterr.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-08-05
 */
public interface EduChapterService extends IService<EduChapter> {
    //课程大纲列表，根据课程id进行查询
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    //删除章节的方法
    boolean deleteChapter(String chapterId);

    //2.根据课程id删除章节
    void removeChapterById(String courseId);
}
