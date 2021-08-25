package com.kenProject.Servicebase.exceptionHandler;

import com.kenProject.commonutils.ExceptionUtil;
import com.kenProject.commonutils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //指定出现了什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody//为了能够返回数据
    public Result error(Exception e){
        e.printStackTrace();
        return Result.error().message("执行了全局异常处理");
    }
    //特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody//为了能够返回数据
    public Result error2(Exception e){
        e.printStackTrace();
        return Result.error().message("执行了算数异常处理");
    }
    //自定义异常
    @ExceptionHandler(KenProjectException.class)
    @ResponseBody//为了能够返回数据
    public Result error3(KenProjectException e){
//        log.error(e.getMessage());//将异常信息写到日志中去
        log.error(ExceptionUtil.getMessage(e));
        e.printStackTrace();
        return Result.error().code(e.getCode()).message(e.getMsg());
    }
}
