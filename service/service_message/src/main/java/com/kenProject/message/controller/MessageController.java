package com.kenProject.message.controller;

import com.kenProject.commonutils.Result;
import com.kenProject.message.service.MsmService;
import com.kenProject.message.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MessageController {

    @Autowired
    private MsmService msmService;

    //redis设置过期时间
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //发送短信的方法
    @GetMapping("send/{phone}")
    public Result sendMsm(@PathVariable String phone) {
        //1、从redis中获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return Result.ok();
        }
        //2、如果redis获取不到，进行阿里云发送

        //生成随机数，传递阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        //调用service的方法，发送
        boolean isSend = msmService.send(param, phone);
        if (isSend) {
            //阿里云发送成功，把发送成功的验证码放入redis缓存中
            //设置有效时间
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return Result.ok();
        } else
            return Result.error().message("短信发送失败");
    }
}