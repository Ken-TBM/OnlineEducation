package com.kenProject.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.kenProject.Servicebase.exceptionHandler.KenProjectException;
import com.kenProject.commonutils.Result;
import com.kenProject.vod.Util.ConstantVodUtils;
import com.kenProject.vod.Util.InitObject;
import com.kenProject.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduVod/video")
@CrossOrigin
public class VodController {
    @Autowired
    private VodService vodService;
    //上传视频到阿里云
    @PostMapping("uploadVideo")
    public Result uploadVideo(MultipartFile file){
        String videoId=vodService.uploadVideoAly(file);
        return Result.ok().data("videoId",videoId);
    }

    //根据视频id删除阿里云视频
    @DeleteMapping("removeAlyVideo/{id}")
    public Result removeAlyVideo(@PathVariable String id){
        try{
            //初始化对象
            DefaultAcsClient client = InitObject.initVodClient
                    ("LTAI5tRPoQ8YBUDus7F5RsTX", "Jg4ZYNvWIJBWskW6ZZ5KXycJcNNZMN");
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request设置视频id
            request.setVideoIds(id);
            //调用对象初始化对象的方法实现删除
            client.getAcsResponse(request);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            throw new KenProjectException(20001,"删除视频失败");
        }
    }

    //删除多个阿里云视频的方法
    //参数多个视频id
    @DeleteMapping("delete-batch")
    public Result deleteBatch(@RequestParam("videoIdList") List<String> videoList){
        vodService.removeMoreAlyVideo(videoList);
        return Result.ok();
    }


    //根据视频id获取视频凭证
    @GetMapping("getPlayAuth/{id}")
    public Result getPlayAuth(@PathVariable String id){
        try{
            //创建初始化对象
            DefaultAcsClient client =
                    InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);

            //创建获取凭证的request和response对象
            GetVideoPlayAuthRequest request=new GetVideoPlayAuthRequest();

            //向request设置视频id
            request.setVideoId(id);

            //调用方法得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return Result.ok().data("playAuth",playAuth);
        }catch (Exception e){
            throw new KenProjectException(20001,"获取凭证失败");
        }
    }


}
