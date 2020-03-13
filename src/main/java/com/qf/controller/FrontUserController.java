package com.qf.controller;
import com.qf.entity.FrontUser;
import com.qf.result.ResultDate;
import com.qf.service.IFrontUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 前台用户Controller
 */
@Controller
@RequestMapping("/frontUserController")
public class FrontUserController {

    @Autowired
    private IFrontUserService iFrontUserService;

    /**
     * 前台用户注册
     * @param user
     * @param code
     * @param session
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public ResultDate register(FrontUser user, String code, HttpSession session){
        return iFrontUserService.register(user,code,session);
    }

    /**
     * 前台用户登录
     * @param user
     * @param returnUrl
     * @param response
     * @return
     */
    //7.登录
    @RequestMapping("/login")
    @ResponseBody
    public ResultDate login(FrontUser user, @RequestParam("returnUrl") String returnUrl, HttpServletResponse response){
        return iFrontUserService.login(user,returnUrl,response);
    }

    /**
     * 用来判断前台用户是否已经登录
     * @param loginToken
     * @return
     */
    @RequestMapping("/isLogin")
    @ResponseBody
    public ResultDate<FrontUser> isLogin(@CookieValue(value = "login_token", required = false) String loginToken) {
        return iFrontUserService.isLogin(loginToken);
    }

    /**
     * 前台用户注销登录
     * @param loginToken
     * @return
     */
   @RequestMapping("/logout")
   @ResponseBody
   public ResultDate logout(@CookieValue(value = "login_token", required = false) String loginToken,HttpServletResponse response){
       iFrontUserService.logout(loginToken,response);
        return new ResultDate().setResult(true).setData("退出成功");
   }
}
