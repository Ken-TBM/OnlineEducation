package com.kenProject.eduService.controller;


import com.kenProject.Servicebase.exceptionHandler.KenProjectException;
import com.kenProject.commonutils.Result;
import com.kenProject.eduService.client.VodClient;
import com.kenProject.eduService.entity.EduVideo;
import com.kenProject.eduService.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-05
 */
@RestController
@RequestMapping("/eduService/edu-video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;

    //注入vodClient
    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public Result addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);;
        return Result.ok();
    }

    //删除小节
    //TODO 后面这个方法需要完善，删除小节的时候，要把里面的视频也删除了
    @DeleteMapping("{id}")
    public Result deleteVideo(@PathVariable String id){
        //先根据小节id获取视频id，调用方法实现视频删除
        EduVideo video = eduVideoService.getById(id);
        String sourceId = video.getVideoSourceId();
//        System.out.println(sourceId);
        //判断小节里是否有视频
        if (sourceId!=null){
            //根据视频id，远程调用实现视频删除
            Result result = vodClient.removeAlyVideo(sourceId);
            if (result.getCode() ==250){
                throw new KenProjectException(200,"删除视频失败======= 熔断器");
            }
        }
        //删除小节
        eduVideoService.removeById(id);
        return Result.ok();
    }
}

