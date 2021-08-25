package com.kenProject.eduOrder.client;

import com.kenProject.commonutils.ordervo.UCenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    //根据用户id获取用户信息
    @PostMapping("/educenter/ucenter-member/getUserInfoOrder/{id}")
    public UCenterMemberOrder getUserInfoOrder(@PathVariable("id") String id);
}
