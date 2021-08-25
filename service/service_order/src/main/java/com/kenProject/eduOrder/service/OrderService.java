package com.kenProject.eduOrder.service;

import com.kenProject.eduOrder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-08-21
 */
public interface OrderService extends IService<Order> {
    //创建订单，返回订单号
    String createOrders(String courseId, String memberIdByJwtToken);
}
