package com.kenProject.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kenProject.Servicebase.exceptionHandler.KenProjectException;
import com.kenProject.eduService.entity.EduChapter;
import com.kenProject.eduService.entity.EduVideo;
import com.kenProject.eduService.entity.chapterr.ChapterVo;
import com.kenProject.eduService.entity.chapterr.VideoVo;
import com.kenProject.eduService.mapper.EduChapterMapper;
import com.kenProject.eduService.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenProject.eduService.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-08-05
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    //课程大纲列表，根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        //1 根据课程id查询课程的所有章节
        QueryWrapper<EduChapter> wrapperChapter=new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2 再根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> wrapperVideo=new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(wrapperVideo);

        //创建list集合，用于最终封装数据
        List<ChapterVo> finalList = new ArrayList<>();


        //遍历查询章节list集合
        for (int i = 0; i < eduChapterList.size(); i++) {
            //每个章节
            EduChapter eduChapter=eduChapterList.get(i);
            //eduChapter对象值复制到ChapterVo里面
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            //把chapterVo放到最终List集合
            finalList.add(chapterVo);

            List<VideoVo> twoFinalList = new ArrayList<>();
            //3 遍历查询小节list集合 并进行封装
            for (int j = 0; j < eduVideoList.size() ; j++) {
                //得到每个小节
                EduVideo eduVideo = eduVideoList.get(j);
                //判断小节里面的chapterId和章节里的id是否一样
                if (eduVideo.getChapterId().equals(chapterVo.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    twoFinalList.add(videoVo);
                }
            }
            //把一级下面所有的二级分类放在一级分类中
            chapterVo.setChildren(twoFinalList);

        }

        return finalList;
    }

    //删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapter章节id查询小节表，如果查询数据，不进行删除
        QueryWrapper<EduVideo> wrapper=new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(wrapper);

        //判读
        if (count >0){
            //查询出小节进行删除
            throw new KenProjectException(20001,"不能删除");
        }else{//不能查询数据，进行删除
            //删除章节
            int result = baseMapper.deleteById(chapterId);
            return result>0;
        }

    }

    @Override
    public void removeChapterById(String courseId) {
        QueryWrapper<EduChapter> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }


}
