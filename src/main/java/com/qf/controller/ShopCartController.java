package com.qf.controller;

import com.alibaba.fastjson.JSON;
import com.qf.aop.IsLogin;
import com.qf.aop.UserHolder;
import com.qf.entity.FrontUser;
import com.qf.entity.ShopCart;
import com.qf.result.ResultDate;
import com.qf.service.IShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/ShopCartController")
public class ShopCartController {

    @Autowired
    private IShopCartService iShopCartService;

    /**
     *  添加购物车，不是必须要登录才能添加购物车
     * @param gid
     * @param number
     * @return
     */

    @RequestMapping("addShopCart")
    @IsLogin(mustLogin = true)
    public String addShopCart(Integer gid, Integer number, @CookieValue(name = "cartToken", required = false) String cartToken, HttpServletResponse response){
        FrontUser frontUser = UserHolder.getUser();
        cartToken  = iShopCartService.addCart(frontUser, gid, number, cartToken);
        //购物车的token写入cookie中
        Cookie cookie = new Cookie("cartToken", cartToken);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "addCartSuccess";
    }

    /**
     * 查询购物车信息
     * @return
     */

    @RequestMapping("/shopCartList")//TODO 2.查询购物车信息
    @ResponseBody
    @IsLogin
    public String shopCartList(@CookieValue(name = "cartToken", required = false) String cartToken, String callback) {
        //1.拿到登录的用户
        //2.调用购物车服务查询购物车信息
        FrontUser frontUser = UserHolder.getUser();
        List<ShopCart> shopCartList = iShopCartService.shopCartList(frontUser, cartToken);
        System.out.println(iShopCartService);
        return callback != null ? callback + "(" + JSON.toJSONString(shopCartList) + ")" : JSON.toJSONString(shopCartList);
    }

    /**
     * 购物车所有商品数量和价格之和
     * @param cartToken
     * @return
     */
    @RequestMapping("/selectAllGoodsNumberAndPrice")
    @ResponseBody
    public ResultDate selectAllGoodsNumberAndPrice(@CookieValue(name = "cartToken", required = false) String cartToken){
        FrontUser frontUser = UserHolder.getUser();
        ResultDate resultDate = iShopCartService.selectAllGoodsNumberAndPrice(frontUser, cartToken);
        return resultDate;
    }

    /**
     * 去购物车结算
     * @param map
     * @return
     */
    @RequestMapping("/goCartCheck")
    @IsLogin(mustLogin = true)
    public String goCartCheck(ModelMap map){
        FrontUser frontUser = UserHolder.getUser();
        List<ShopCart> shopCartList = iShopCartService.goCartCheck(frontUser);
        map.put("shopCartList",shopCartList);
        return "shopCart";
    }

    /*TODO 加减商品数量*/
    /**
     * 添加购物车数量
     */
    @RequestMapping("addNumber")
    @ResponseBody
    public ResultDate addNumber(int id){
        ResultDate resultDate = iShopCartService.addNumber(id);

        return resultDate;
    }

    /**
     * 减购物车数量
     */
    @RequestMapping("cutNumber")
    @ResponseBody
    public ResultDate cutNumber(int id){
        //productservice.cutNumber(sid)
        iShopCartService.cutNumber(id);
        return new ResultDate().setResult(true);
    }

}
