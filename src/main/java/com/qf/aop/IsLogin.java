package com.qf.aop;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)//1.这个注解是加在方法上的
@Retention(RetentionPolicy.RUNTIME)//2.需要用到反射来操作这个注解
public @interface IsLogin {
    //3.注解里面的方法的定义规则：方法的返回值类型 方法名() [default 默认值];
    boolean mustLogin() default false;
}
