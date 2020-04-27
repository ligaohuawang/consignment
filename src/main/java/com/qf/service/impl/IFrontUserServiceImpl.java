package com.qf.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.entity.FrontUser;
import com.qf.mapper.IFrontUserMapper;
import com.qf.result.ResultDate;
import com.qf.service.IFrontUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class IFrontUserServiceImpl implements IFrontUserService {
    @Autowired
    private IFrontUserMapper iFrontUserMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 前台用户注册
     * @param user
     * @param code
     * @param session
     * @return
     */
    @Override
    public ResultDate register(FrontUser user, String code, HttpSession session) {
        String chCode = (String)session.getAttribute("chCode");
        ResultDate resultDate = new ResultDate();
        code = code.toLowerCase();
        chCode = chCode.toLowerCase();
        if (!(code.equals(chCode))){
            //验证码错误
            resultDate.setResult(false);
            resultDate.setData("验证码错误！");
        }else{
            //验证码正确
            //先判断用户是否已经注册了
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("username", user.getUsername());
            FrontUser frontUser = iFrontUserMapper.selectOne(queryWrapper);

            if (frontUser==null){
                //没有注册过
                //进行密码加密,得到加密后的密码
                String password2 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
                user.setPassword(password2);//重新设置密码
                int result = iFrontUserMapper.insert(user);
                if(result>0){
                    //注册成功
                    resultDate.setResult(true);
                }else{
                    //注册失败
                    resultDate.setResult(false);
                    resultDate.setData("注册失败！");
                }
            }else {
                //已经注册过了
                resultDate.setResult(false);
                resultDate.setData("用户名已经注册！");
            }
        }
        return resultDate;
    }

    /**
     * 前台用户登录
     * @param user
     * @return
     */
    @Override
    public ResultDate login(FrontUser user, String returnUrl, HttpServletResponse response) {

        //如果没有调整的url
        if (returnUrl == null || returnUrl.equals("")) {
            //默认跳转到首页
            returnUrl = "CheShiController/index";
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",user.getUsername());
        FrontUser frontUser = iFrontUserMapper.selectOne(queryWrapper);
        ResultDate resultDate = new ResultDate();
        //将用户输入的密码MD5加密，用于和数据库用户密码进行比较
        String password3 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        if (frontUser!=null&&frontUser.getPassword().equals(password3)){
            //登录成功
            resultDate.setResult(true);
            resultDate.setData("登录成功");
            resultDate.setReturnUrl(returnUrl);
            //后续操作，比如保存登录的用户的消息。。。
            //登录成功，将用户信息写入redis
            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(token, frontUser);
            redisTemplate.expire(token, 7, TimeUnit.DAYS);

            //将uuid写入cookie
            Cookie cookie = new Cookie("login_token", token);
            //设置cookie的最大存活时间，如果不设置，默认浏览器关闭就没有了，单位是秒
            cookie.setMaxAge(60 * 60 * 24 * 7);
            cookie.setPath("/");
            //将cookie写入浏览器 - Response
            response.addCookie(cookie);
        }else {
            resultDate.setResult(false);
            resultDate.setData("登录失败，用户名或密码有误！");
        }
        return resultDate;
    }

    /**
     * 用来判断用户是否已经登录
     * @param loginToken
     * @return
     */
    @Override
    public ResultDate<FrontUser> isLogin(String loginToken) {
        //去redis中验证是否登录
        if (loginToken != null) {
            //去redis中验证是否登录
            FrontUser user = (FrontUser) redisTemplate.opsForValue().get(loginToken);
            //刷新cookie和redis的超时时间

            if (user != null) {
                //已经登录
                return new ResultDate<FrontUser>().setResult(true).setResultDate(user);
            }
        }
        return new ResultDate<FrontUser>().setResult(false).setResultDate(null);
    }

    /**
     * 前台用户注销登录
     * @param loginToken
     */
    @Override
    public void logout(String loginToken,HttpServletResponse response) {
        if(loginToken!=null){
            //删除redis
            redisTemplate.delete(loginToken);

            //删除cookie
            Cookie cookie = new Cookie("login_token", loginToken);
            cookie.setMaxAge(0);//设置为0表示删除该cookie
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Override
    public FrontUser selectUserByUsername(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        FrontUser frontUser = iFrontUserMapper.selectOne(queryWrapper);
        return frontUser;
    }

    /**
     * 根据用户名修改密码
     * @param username
     * @param newpassword
     */
    @Override
    public void updatePassword(String username, String newpassword) {
    //先根据用户名查询出用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        FrontUser frontUser = iFrontUserMapper.selectOne(queryWrapper);

        //修改密码前先将新密码进行MD5加密
        String newpassword2 = DigestUtils.md5DigestAsHex(newpassword.getBytes(StandardCharsets.UTF_8));
        frontUser.setPassword(newpassword2);
        //将用户设置回去
        iFrontUserMapper.update(frontUser,queryWrapper);
    }
}
