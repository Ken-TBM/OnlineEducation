package com.kenProject.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.kenProject.oss.service.OssService;
import com.kenProject.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传头像到OOS
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtils.ACCES_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCES_KEY_SECRET;
        String bucketName= ConstantPropertiesUtils.BUCKET_NAME;

        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        InputStream inputStream = null;
        OSS ossClient =null;
        try {
            // 创建OSSClient实例。
            ossClient=new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //获取上传文件流
            inputStream = file.getInputStream();
            //获取文件名称
            String filename = file.getOriginalFilename();

            //在文件名称中随机添加唯一值
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            filename = uuid + filename;

            //把文件按照日期进行分类
            //2021/8/3/01.jpg
            //获取日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            filename=datePath+"/"+filename;

            //第一个参数 Bucket名称
            //第二个参数 上传到Oss文件和文件路径和名称
            //第三个参数 上传文件输入流
            // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName,filename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后的文件路径返回
            //需要把上传到阿里云Oss路径手动拼接出来
            //https://education-ken.oss-cn-beijing.aliyuncs.com/7-1.jpg
            String url="https://"+bucketName+"."+endpoint+"/"+filename;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }




        return null;
    }
}
