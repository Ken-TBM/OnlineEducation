package com.kenProject.vod.service.ipml;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.utils.StringUtils;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.kenProject.Servicebase.exceptionHandler.KenProjectException;
import com.kenProject.commonutils.Result;
import com.kenProject.vod.Util.InitObject;
import com.kenProject.vod.service.VodService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    @Override
    public String uploadVideoAly(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            UploadStreamRequest request = new UploadStreamRequest(
                    "LTAI5tRPoQ8YBUDus7F5RsTX",
                    "Jg4ZYNvWIJBWskW6ZZ5KXycJcNNZMN",
                    title, originalFilename, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。

            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            String videoId = response.getVideoId();
            if (!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
                if(StringUtils.isEmpty(videoId)){
                    throw new KenProjectException(20001, errorMessage);
                }
            }
            return videoId;
        } catch (IOException e) {
            throw new KenProjectException(20001, "服务上传失败");
        }
    }

    @Override
    public void removeMoreAlyVideo(List videoList) {
        try{
            //初始化对象
            DefaultAcsClient client = InitObject.initVodClient
                    ("LTAI5tRPoQ8YBUDus7F5RsTX", "Jg4ZYNvWIJBWskW6ZZ5KXycJcNNZMN");
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            String videoIds = org.apache.commons.lang.StringUtils.join(videoList.toArray(), ",");
            //向request设置视频id
            request.setVideoIds(videoIds);
            //调用对象初始化对象的方法实现删除
            client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
            throw new KenProjectException(20001,"删除视频失败");
        }
    }

}
