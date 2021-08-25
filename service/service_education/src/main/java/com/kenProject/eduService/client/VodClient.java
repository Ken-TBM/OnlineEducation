package com.kenProject.eduService.client;

import com.kenProject.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)//调用服务类名称
public interface VodClient {
    //定义调用的方法路径
    //根据视频id删除阿里云视频
    //@PathVariable注解一定要指定参数名称，否则出错
    @DeleteMapping("/eduVod/video/removeAlyVideo/{id}")
    public Result removeAlyVideo(@PathVariable("id") String id);

    //定义调用多个视频的方法
    //删除多个阿里云视频
    //参数 多个视频id List
    @DeleteMapping("/eduVod/video/delete-batch")
    public Result deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);

}
