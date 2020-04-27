package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.aop.UserHolder;
import com.qf.entity.Application;
import com.qf.entity.FrontUser;
import com.qf.entity.PurchaseOrder;
import com.qf.result.ResultDate;
import com.qf.service.IPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/PurchaseOrderController")
public class PurchaseOrderController {

    @Autowired
    private IPurchaseOrderService iPurchaseOrderService;

    //TODO F3 做添加商品前的准备
    @RequestMapping("applyForSell")
    public String toAddGoods(){
        return "applyForSell";
    }

    //TODO G3 接收ajax提交的代销申请信息
    @RequestMapping("/addApplication")
    @ResponseBody
    @IsLogin(mustLogin = true)
    public ResultDate addApplication(Application application){
        FrontUser user = UserHolder.getUser();
        if (application!=null){
          application.setUid(user.getId());
        }
        iPurchaseOrderService.addApplication(application);
return new ResultDate().setResult(true);
    }

    //TODO H2 controller接收请求查询代销申请。返回页面放置代销申请
    @RequestMapping("/myApplyForSell")
    @IsLogin(mustLogin = true)
    public String myApplyForSell(ModelMap map){
        FrontUser user = UserHolder.getUser();
       List<Application> applicationList = iPurchaseOrderService.myApplyForSell(user.getId());
       map.put("applicationList",applicationList);
        return "myApplyForSell";
    }

    //TODO J2 controller接收请求查询采购订单。返回页面放置采购订单
    @RequestMapping("/myPurchaseOrders")
    @IsLogin(mustLogin = true)
    public String myPurchaseOrders(ModelMap map){
        FrontUser user = UserHolder.getUser();
        List<PurchaseOrder> purchaseOrderList = iPurchaseOrderService.myPurchaseOrders(user.getId());
        map.put("purchaseOrderList",purchaseOrderList);
        return "myPurchaseOrderList";
    }

    //TODO K3 controller接收请求fahuo
    @RequestMapping("/fahuo")
    @ResponseBody
    public ResultDate fahuo(Integer id){
        ResultDate resultDate= iPurchaseOrderService.fahuo(id);
        return resultDate;
    }



}
