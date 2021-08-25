package com.kenProject.educenter.controller;


import com.kenProject.commonutils.JwtUtils;
import com.kenProject.commonutils.Result;
import com.kenProject.commonutils.ordervo.UCenterMemberOrder;
import com.kenProject.educenter.entity.UcenterMember;
import com.kenProject.educenter.entity.vo.RegisterVo;
import com.kenProject.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/educenter/ucenter-member")
@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    //电话和密码登录
    @PostMapping("login")
    public Result login(@RequestBody UcenterMember ucenterMember){
        String token =ucenterMemberService.login(ucenterMember);
        System.out.println("成功！");
        return Result.ok().data("token",token);
    }
    //注册
    @PostMapping("register")
    public Result register(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return Result.ok();
    }
    //根据token获取前端信息
    @GetMapping("getMemberInfo")
    public Result getMemberInfo(HttpServletRequest request){
        //调用jwt工具类，获取头部信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据id获得用户信息
        UcenterMember member = ucenterMemberService.getById(memberId);
        System.out.println("根据token获取到member值："+member);
        return Result.ok().data("userInfo",member);
    }

    //根据用户id获取用户信息
    @PostMapping("getUserInfoOrder/{id}")
    public UCenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember member = ucenterMemberService.getById(id);
        UCenterMemberOrder uCenterMemberOrder = new UCenterMemberOrder();
        //把member对象里的值复制给UCenterMemberOrder对象
        BeanUtils.copyProperties(member,uCenterMemberOrder);
        return uCenterMemberOrder;
    }

    //查询某一天注册人数
    @GetMapping("countRegister/{day}")
    public Result countRegister(@PathVariable String day){
        Integer count= ucenterMemberService.countRegisterDay(day);
        return Result.ok().data("countRegister",count);
    }
}

