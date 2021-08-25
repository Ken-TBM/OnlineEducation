package com.kenProject.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenProject.commonutils.Result;
import com.kenProject.educms.entity.CrmBanner;
import com.kenProject.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-15
 */
@RestController
@RequestMapping("/educms/bannerAdmin")
@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    //1 分页查询
    @GetMapping("pageBanner/{page}/{limit}")
    public Result pageBanner(@PathVariable long page,@PathVariable long limit){
        Page<CrmBanner> pageBanner= new Page<>(page,limit);
        bannerService.page(pageBanner,null);

        return Result.ok().data("items",pageBanner.getRecords())
                .data("total",pageBanner.getTotal());
    }

    //添加banner
    @PostMapping("addBanner")
    public Result addBanner(@RequestBody CrmBanner crmBanner){
        bannerService.save(crmBanner);
        return Result.ok();
    }

    @ApiOperation("获取Banner")
    @GetMapping("get/{id}")
    public Result get(@PathVariable String id){
        CrmBanner banner = bannerService.getById(id);
        return Result.ok().data("item",banner);
    }

    //修改Banner
    @ApiOperation(value = "修改Banner")
    @PutMapping("update")
    public Result update(@RequestBody CrmBanner crmBanner){
        bannerService.updateById(crmBanner);
        return Result.ok();
    }

    @ApiOperation(value = "删除banner")
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable String id){
        bannerService.removeById(id);
        return Result.ok();
    }

}

