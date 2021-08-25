package com.kenProject.educms.controller;

import com.kenProject.commonutils.Result;
import com.kenProject.educms.entity.CrmBanner;
import com.kenProject.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/educms/bannerFront")
@CrossOrigin
public class BannerFrontController {
    @Autowired
    private CrmBannerService bannerService;

    //查询所有banner
    @GetMapping("getAllBanner")
    public Result getAllBanner(){
        List<CrmBanner> list=bannerService.selectAllBanner();
        return Result.ok().data("list",list);
    }



}
