package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.aop.UserHolder;
import com.qf.entity.Address;
import com.qf.entity.FrontUser;
import com.qf.entity.Orders;
import com.qf.entity.ShopCart;
import com.qf.result.ResultData;
import com.qf.service.IAddressService;
import com.qf.service.IOrderService;
import com.qf.service.IShopCartService;
import com.qf.utils.PriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/orderController")
public class OrderController {

    @Autowired
    private IShopCartService iShopCartService;
    @Autowired
    private IAddressService iAddressService;
    @Autowired
    private IOrderService iOrderService;

    //TODO D2 controller接收from表单去创建订单
    @IsLogin(mustLogin = true)
    @RequestMapping("/toCreateOrder")
    public String toCreateOrder(Integer[] checkitems, Model model){
        //1.先得到登录用户的信息
        //2.根据用户id和商品id去查询商品信息
        //3.根据用户id查询用户的收货地址
        FrontUser frontUser = UserHolder.getUser();
        List<ShopCart> shopCarts = iShopCartService.queryCartsByGidAndUid(checkitems, frontUser);
        System.out.println("1.所有的购物车信息："+shopCarts);
        //用户所有的收货地址
        List<Address> addresses = iAddressService.selectAllAddressByUid(frontUser.getId());
        System.out.println("2.所有的地址信息："+addresses);
        //计算总价给页面
        double totalprice = PriceUtil.allPrice(shopCarts);
        System.out.println("3.总价："+totalprice);
        model.addAttribute("carts", shopCarts);
        model.addAttribute("addresses", addresses);
        model.addAttribute("allprice", totalprice);
        return "ordersConfirm";
    }


    @IsLogin(mustLogin = true)
    @ResponseBody
    @RequestMapping("/submitCreateOrder")
    public ResultData<Orders> submitCreateOrder(Integer[] cids, Integer aid, String expressmethod, String paymethod) {
        FrontUser frontUser = UserHolder.getUser();
        //调用方法添加订单并生成订单
        Orders orders = iOrderService.createOrder(cids,aid,frontUser,expressmethod,paymethod);
        return new ResultData<Orders>().setCode(ResultData.ResultCodeList.OK).setMsg("下单成功！").setData(orders);
    }

    //TODO E3 接收请求查询用户订单并返回给页面
    @RequestMapping("/selectUserOrder")
    public String selectUserOrder(Integer id, ModelMap map){
    List<Orders> ordersList=iOrderService.selectUserOrder(id);
    map.put("ordersList",ordersList);
      return "orderList";
    }

}
