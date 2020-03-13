package com.qf.aop;

import com.qf.entity.FrontUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

@Component//1.让spring扫到，用来创建代理对象
@Aspect//2.表示这个类是切面类
public class LoginAop {
    @Autowired
    private RedisTemplate redisTemplate;

@Around("@annotation(IsLogin)")//切点表达式，表示加了@IsLogin注解的目标方法会被织入环绕增强，然后被spring创建出代理对象
public Object loginAop(ProceedingJoinPoint proceedingJoinPoint) {
//----------1、获得cookie--------------
    //获得请求
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();
    //获得cookie
    String loginToken = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login_token")) {
                loginToken = cookie.getValue();
                break;
            }
        }
    }
//----------2、通过登录令牌判断是否登录----------
    FrontUser frontUser = null;
    if (loginToken != null) {
        //redis中获得用户
        frontUser = (FrontUser) redisTemplate.opsForValue().get(loginToken);
        if (frontUser != null) {
            UserHolder.setUser(frontUser);
        } else {
            //不做处理
        }
    }
//----------3、判断是否登录-----------------
    if (frontUser == null) {
        //当前未登录
        //获得@IsLogin注解，并获得注解里的方法，看看是不是强制登录
        //获得方法签名
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        //获得方法的反射对象，getMethod()获得所有的公有方法，这个方法连父类中的方法也会拿过来
        Method method = signature.getMethod();
        //通过反射对象获得方法上的注解，@IsLogin注解
        IsLogin islogin = method.getAnnotation(IsLogin.class);
        //调用注解的方法，获得是true还是false
        boolean flag = islogin.mustLogin();
        //调用它的mustLogin方法，获得方法返回值
        if (flag) {//true表示需要强制登录
            //获得当前的url
            String returnUrl = request.getRequestURL().toString() + "?" + request.getQueryString();
            //url编码
            try {
                returnUrl = URLEncoder.encode(returnUrl, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //
            String loginUrl = "http://localhost:8081/CheShiController/login?returnUrl=" + returnUrl;
            return "redirect:"+ loginUrl;
        }
    }
    //如果用户已经登录，user就不为空，如果他还是不登录user就还是为空
    Object result = null;
    try {
        //执行目标方法及返回值，就是在这里去调用目标方法的
        result = proceedingJoinPoint.proceed();
    } catch (Throwable throwable) {
        throwable.printStackTrace();
    }
    //清空threadlocal
    UserHolder.setUser(null);
    return result;
}
}
