package com.kenProject.statservice.schedule;

import com.kenProject.statservice.service.StatisticsDailyService;
import com.kenProject.statservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //每隔五秒
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1(){
//        System.out.println("***************task1执行了!!!");
//    }

    //在每天凌晨一点，把前一天的数据进行数据查询添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2(){
        statisticsDailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.formatDate(new Date()));
        System.out.println();
    }
}

