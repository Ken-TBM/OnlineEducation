package com.kenProject.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kenProject.Servicebase.exceptionHandler.KenProjectException;
import com.kenProject.commonutils.JwtUtils;
import com.kenProject.commonutils.MD5;
import com.kenProject.educenter.entity.UcenterMember;
import com.kenProject.educenter.entity.vo.RegisterVo;
import com.kenProject.educenter.mapper.UcenterMemberMapper;
import com.kenProject.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-08-17
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //登录的方法
    @Override
    public String login(UcenterMember ucenterMember) {
        //手机号和密码做登录
        //1、获取手机号和密码
        String mobile = ucenterMember.getMobile();
        String password = ucenterMember.getPassword();
        System.out.println(mobile+"========="+password);
         if (StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            throw new KenProjectException(20001,"手机号和密码为空");
        }
        //判断手机号是否正确
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(queryWrapper);
        if (mobileMember == null){
            throw new KenProjectException(20001,"手机号不存在");
        }

        //判断密码是否相等
        //数据库密码进行加密，不能直接对比
        //MD5对密码进行加密，再与数据库进行比较
        String password1 = mobileMember.getPassword();
        if (!MD5.encrypt(password).equals(password1)){
            throw new KenProjectException(20001,"密码错误");
        }

        //判断用户是否被禁用
        if(mobileMember.getIsDisabled()){
            throw new KenProjectException(20001,"用户被禁用登录失败");
        }

        //登录成功
        //按照jwt生产token返回
        String token = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return token;
    }

    //注册的方法
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode();//验证码
        String mobile = registerVo.getMobile();//手机号
        String nickname = registerVo.getNickname();//昵称
        String password = registerVo.getPassword();//密码

        //非空判断
        if (StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)
                ||StringUtils.isEmpty(code)||StringUtils.isEmpty(nickname)){
            throw new KenProjectException(20001,"登录失败");
        }

        //判断验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)){
            throw new KenProjectException(20001,"注册失败");
        }

        //判断的手机号是否重估，表里存在相同的手机号则不添加
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count>0){
            throw new KenProjectException(20001,"手机号已存在");
        }
        //数据添加数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));//密码加密
        member.setIsDisabled(false);
        member.setAvatar("https://education-ken.oss-cn-beijing.aliyuncs.com/2021/08/16/398b42f743db4736a0bac6730409ea06file.png");
        baseMapper.insert(member);

    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    //查询某一天注册人数
    @Override
    public Integer countRegisterDay(String day) {
        Integer count = baseMapper.countRegisterDay(day);
        return count;
    }
}
