package com.qf.controller;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qf.entity.Orders;
import com.qf.mapper.IPurchaseOrderMapper;
import com.qf.service.IOrderService;
import com.qf.service.IPurchaseOrderService;
import com.qf.utils.AlipayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IPurchaseOrderService iPurchaseOrderService;


    //TODO C 支付宝支付
    @RequestMapping("/alipay")
    @ResponseBody
    public void alipay(String orderid, HttpServletResponse response) throws IOException {
        Orders orders = iOrderService.QueryByOid(orderid);
        //1.创建一个支付宝的客户端对象（支付、退款、查询、关闭交易....都由这个对象来做）
        AlipayClient alipayClient = AlipayUtil.getAlipayClient();
        //2.创建一个支付页面请求对象
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        //3.设置支付完成后的同步请求（支付完成后的跳转页面）
        alipayRequest.setReturnUrl("http://localhost:8081");//http://lgh88.qicp.vip相当于localhost:8081，但是loginTocken是localhost域名的，所以要使用localhost不然登录状态就是未登录了
        //4.设置支付完成后的异步请求 - 决定支付是否成功
        alipayRequest.setNotifyUrl("http://lgh88.qicp.vip/pay/callBack");
        //5.填充业务参数
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" +orders.getOid()+ "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + orders.getAllprice().doubleValue() + "," +
                "    \"subject\":\"" + orders.getOrderDetails().get(0).getSubject() + "..\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }" +
                "  }");

        //6.发送请求给支付宝，支付宝返回一个支付页面
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        //7.直接将支付页面返回给用户浏览器
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    @RequestMapping("/callBack")
    @ResponseBody
    public String payCallBack(String out_trade_no, String trade_status, HttpServletRequest request) throws AlipayApiException {
        Map<String, String> signParams = new HashMap<>();
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            signParams.put(entry.getKey(), entry.getValue()[0]);
        }
        //验签
        boolean signVerified = AlipaySignature.rsaCheckV1(signParams, AlipayUtil.ALIPAY_PUBLICK_KEY, signParams.get("charset"), signParams.get("sign_type"));
        if (signVerified) {
            if (trade_status.equals("TRADE_SUCCESS") || trade_status.equals("TRADE_FINISHED")) {
                //支付成功，修改订单状态
                iOrderService.updateOrderStatus(out_trade_no, 1);
                iPurchaseOrderService.updateOrderStatus(out_trade_no, 1);
                return "success";
            }
        } else {
            System.out.println("支付验证失败！");
        }
        return "failure";
    }
}
