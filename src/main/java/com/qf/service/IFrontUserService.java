package com.qf.service;

import com.qf.entity.FrontUser;
import com.qf.result.ResultDate;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface IFrontUserService {
    /**
     * 前台用户注册
     * @param user
     * @param code
     * @param session
     * @return
     */
    ResultDate register(FrontUser user, String code, HttpSession session);

    /**
     * 前台用户登录
     * @param user
     * @return
     */
    ResultDate login(FrontUser user, String returnUrl,HttpServletResponse response);

    /**
     * 用来判断是否已经登录
     * @param loginToken
     * @return
     */
    ResultDate<FrontUser> isLogin(String loginToken);

    /**
     * 前台用户注销登录
     * @param loginToken
     */
    void logout(String loginToken,HttpServletResponse response);
}
