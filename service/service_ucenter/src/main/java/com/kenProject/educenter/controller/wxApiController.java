package com.kenProject.educenter.controller;

import com.google.gson.Gson;
import com.kenProject.Servicebase.exceptionHandler.KenProjectException;
import com.kenProject.commonutils.JwtUtils;
import com.kenProject.educenter.entity.UcenterMember;
import com.kenProject.educenter.service.UcenterMemberService;
import com.kenProject.educenter.utils.ConstantWxUtils;
import com.kenProject.educenter.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller //只是想请求地址，不需要返回数据
@RequestMapping("/api/ucenter/wx")
public class wxApiController {

    @Autowired
    private UcenterMemberService memberService;

    //1.生成微信扫描的二维码
    @GetMapping("login")
    public String getWxCode(){
        //固定地址，后面拼接参数
        // 微信开放平台授权baseUrl

        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
        "?appid=%s" +
        "&redirect_uri=%s" +
        "&response_type=code" +
        "&scope=snsapi_login" +
        "&state=%s" +
        "#wechat_redirect";

        //对redirect_url进行URLEncode编码
        String redirect_url=ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirect_url = URLEncoder.encode(redirect_url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirect_url,
                "kenProject"
        );
        //重定向到请求微信地址
        return "redirect:"+url;
    }

    //2 获取扫描人信息，添加数据
    @GetMapping("callback")
    public String callBack(String code,String state) {
        try {
            // 1 获取code值,临时票据，类似验证码
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            //拼接三个参数：id 密钥 和 code值
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code);
            //请求这个拼接好的地址，得到返回两个值
            //使用httpClient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo:"+accessTokenInfo);
            // 2 拿着code值请求微信固定的地址，得到两个值 access_token和openid
            //把accessTokenInfo字符串转换为map集合，根据map里面key获取对应值】
            //使用json转换工具Gson
            Gson gson=new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");



            //把扫码人信息添加到数据库中
            //判断数据是否存在相同的微信信息，根据openid判断
            UcenterMember member=memberService.getOpenIdMember(openid);

            if (member == null){
                //3\拿着access_token和openid，再去请求微信提供的固定地址，获取扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";

                //再次拼接微信地址
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);

                String userInfo = HttpClientUtils.get(userInfoUrl);

                //获取的微信个人信息json信息进行转换
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String)userInfoMap.get("nickname");//昵称
                String headimgurl = (String)userInfoMap.get("headimgurl");//头像

                //把微信信息注册到数据库中
                member = new UcenterMember();
                member.setNickname(nickname);
                member.setOpenid(openid);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }

            //使用jwt根据member对象生成token字符串
            String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            return "redirect:http://localhost:3000?token="+token;
        } catch (Exception e) {
           throw  new KenProjectException(20001,"登录失败");
        }

    }
}



