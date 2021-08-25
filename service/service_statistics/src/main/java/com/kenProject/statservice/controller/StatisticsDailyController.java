package com.kenProject.statservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kenProject.commonutils.Result;
import com.kenProject.statservice.entity.StatisticsDaily;
import com.kenProject.statservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/statservice/statistics-daily")
@CrossOrigin
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //统计某一天注册人数,生成统计数据
    @PostMapping("registerCount/{day}")
    public Result register(@PathVariable String day){
       statisticsDailyService.registerCount(day);
       System.out.println("生成了统计数据");
       return Result.ok();
    }


    //图表显示，返回两部分数据，日期json数组，数量json数组
    @GetMapping("showData/{type}/{begin}/{end}")
    public Result showData(@PathVariable String type,@PathVariable String begin
                            ,@PathVariable String end){
        System.out.println(type);
        System.out.println(begin);
        System.out.println(end);
        Map<String,Object> map=statisticsDailyService.getShowData(type,begin,end);
        System.out.println(map);
        return Result.ok().data(map);
    }
}

