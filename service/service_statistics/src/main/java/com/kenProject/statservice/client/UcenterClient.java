package com.kenProject.statservice.client;

import com.kenProject.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    @GetMapping("/educenter/ucenter-member/countRegister/{day}")
    public Result countRegister(@PathVariable("day") String day);
}
