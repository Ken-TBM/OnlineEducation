package com.kenProject.eduService.controller;


import com.kenProject.commonutils.Result;
import com.kenProject.eduService.entity.EduChapter;
import com.kenProject.eduService.entity.chapterr.ChapterVo;
import com.kenProject.eduService.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-05
 */
@RestController
@RequestMapping("/eduService/edu-chapter")
@CrossOrigin
public class EduChapterController {
    @Autowired
    private EduChapterService chapterService;

    //课程大纲列表,根据课程id进行查询
    @GetMapping("getChapterVideo/{courseId}")
    public Result getChapterVide(@PathVariable String courseId){
        List<ChapterVo> list= chapterService.getChapterVideoByCourseId(courseId);
        return Result.ok().data("allChapterVideo",list);
    }

    //添加章节
    @PostMapping("addChapter")
    public Result addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return Result.ok();
    }
    
    //根据章节id查询
    @GetMapping("getChapterInfo/{chapterId}")
    public Result getChapterInfo(@PathVariable String chapterId){
        EduChapter eduChapter = chapterService.getById(chapterId);
        return Result.ok().data("chapter",eduChapter);
    }
    
    //修改章节
    @PostMapping("updateChapter")
    public Result getChapterInfo(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return Result.ok();
    }
    
    //删除的方法
    @DeleteMapping("{chapterId}")
    public Result deleteChapter(@PathVariable String chapterId){
        boolean flag=chapterService.deleteChapter(chapterId);
        if (flag){
            return Result.ok();
        }else {
            return Result.error();
        }
    }

    
}

