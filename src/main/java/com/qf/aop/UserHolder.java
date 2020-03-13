package com.qf.aop;


import com.qf.entity.FrontUser;

public class UserHolder {
    //new一个线程
    private static ThreadLocal<FrontUser> threadLocalUser = new ThreadLocal<>();

    //保存登录状态
    private static boolean isLogin() {
        return threadLocalUser.get() != null;
    }

    public static void setUser(FrontUser user) {
        UserHolder.threadLocalUser.set(user);
    }

    public static FrontUser getUser() {
        return threadLocalUser.get();
    }
}
