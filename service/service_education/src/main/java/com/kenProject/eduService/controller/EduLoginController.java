package com.kenProject.eduService.controller;

import com.kenProject.commonutils.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduService/user")
@CrossOrigin//解决跨域
public class EduLoginController {
    //login
    @PostMapping("login")
    public Result login(){
        return Result.ok().data("token","admin");
    }

    //info
    @GetMapping("info")
    public Result info(){
        return Result.ok().data("roles","[admin]").data("name","admin").data("avatar","https://education-ken.oss-cn-beijing.aliyuncs.com/2021/08/04/dba31cfaa9424dafab9454f983184c8ffile.png");
    }
}
