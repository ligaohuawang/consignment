package com.qf.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qf.entity.FrontUser;
import com.qf.mapper.IFrontUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/CheShiController")
public class CheShiController {
    @Autowired
    private IFrontUserMapper iFrontUserMapper;

    /**
     * 1.首页
     * @return
     */
    @RequestMapping("/index")
    public String returnIndex(){
        return "index";
    }

    /**
     * 2.首页去注册页
     * @return
     */
    @RequestMapping("/registered")
    public String toRegistered(){
        return "registered";
    }

    /**
     * 3.首页去登录页 / 注册成功后去登录页
     * @return
     */
    @RequestMapping("/login")
    public String toLogin(String returnUrl,ModelMap map){
        if(returnUrl!=null&&!"".equals(returnUrl)){
            try {
                 returnUrl = URLEncoder.encode(returnUrl, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            map.put("returnUrl",returnUrl);
        }
        return "login";
    }




    /**
     * 4.首页去用户个人主页
     * @return
     */
    @RequestMapping("/user")
    public String toUser(){
        return "user";
    }


    /**
     * 5.用户个人主页去个人订单页面。首页去个人订单页面
     * @return
     */
    @RequestMapping("/orderList")
    public String toOrderList(){
        return "orderList";
    }
    /**
     * 6.用户个人主页去收货地址页面
     * @return
     */
    @RequestMapping("/address")
    public String toAddress(){
        return "address";
    }

    /**
     * 7.用户个人主页去修改地址页面
     * @return
     */
    @RequestMapping("/changePassword")
    public String toChangePassword(){
        return "changePassword";
    }


    /**
     * 8.首页去产品列表页面
     * @return
     */
    @RequestMapping("/productList")
    public String toProductList(){
        return "productList";
    }

    /**
     * 9.首页去限时特卖页面
     * @return
     */
    @RequestMapping("/limitBuy")
    public String toLimitBuy(){
        return "limitBuy";
    }

    /**
     * 10.首页去购物车结算页面
     * @return
     */
    @RequestMapping("/shopCart")
    public String toShopCart(){
        return "shopCart";
    }


    /**
     * 11.购物车页面去订单确认页面
     * @return
     */
    @RequestMapping("/ordersConfirm")
    public String toOrderConfirm(){
        return "ordersConfirm";
    }

    /**
     * 11.产品列表页面去产品详情页面
     * @return
     */
    @RequestMapping("/productDetails")
    public String toProductDetails(){
        return "productDetails";
    }

    /**
     * 12.用户个人页面去个人信息页面
     * @return
     */
    @RequestMapping("/userPersonalinfo")
    public String toUserPersonalinfo(){
        return "userPersonalinfo";
    }


    @RequestMapping("/selectpage")
    public String selectpage(Page<FrontUser> page,ModelMap map){
        IPage<FrontUser> iPage = iFrontUserMapper.selectPage(page, null);
        page.setRecords(iPage.getRecords());
        map.put("page",page);
        map.put("url","CheShiController/selectpage");
        System.out.println("total结果为"+page.getTotal());
        System.out.println("size结果为"+page.getSize());
        System.out.println("current结果为"+page.getCurrent());
        System.out.println("records结果为"+page.getRecords());
        for (int i=0;i<page.getRecords().size();i++){
            System.out.println(i+"为："+page.getRecords().get(i));
        }
        return "productList";
    }
}
