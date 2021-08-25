package com.kenProject.eduOrder.service.impl;

import com.kenProject.commonutils.ordervo.CourseWebVoOrder;
import com.kenProject.commonutils.ordervo.UCenterMemberOrder;
import com.kenProject.eduOrder.client.EduClient;
import com.kenProject.eduOrder.client.UcenterClient;
import com.kenProject.eduOrder.entity.Order;
import com.kenProject.eduOrder.mapper.OrderMapper;
import com.kenProject.eduOrder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenProject.eduOrder.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-08-21
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private  EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    //创建订单，返回订单号
    @Override
    public String createOrders(String courseId, String memberId) {
        //通过远程调根据用户id用获取用户信息
        UCenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberId);

        //远程调用根据课程id获取课程信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        //创建Order对象，向order对象里面设置需要的数据
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());//订单号
        order.setCourseId(courseId);//课程id
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0);//支付状态
        order.setPayType(1);//支付类型
        baseMapper.insert(order);
        //返回订单号
        return order.getOrderNo();
    }
}
