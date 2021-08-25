package com.kenProject.eduService.client;

import org.springframework.stereotype.Component;

@Component
public class OrdersFileDegradeFeignClient implements OrdersClient {
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        System.out.println("支付订单出错");
        return false;
    }
}
