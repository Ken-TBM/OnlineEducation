package com.kenProject.oss.controller;

import com.kenProject.commonutils.Result;
import com.kenProject.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduOss/fileOss")
@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;

    //上传头像的方法
    @PostMapping
    public Result uploadOssFile(MultipartFile file){
        //获取上传文件
        String url=ossService.uploadFileAvatar(file);
        return Result.ok().data("url",url);
    }

}
