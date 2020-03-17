package com.qf.service;

import com.qf.Base.BaseService;
import com.qf.entity.FrontUser;
import com.qf.entity.Orders;
public interface IOrderService extends BaseService<Orders> {
    /**
     * 下单的操作
     * @param cids
     * @param aid
     * @param frontUser
     * @param expressmethod
     * @param paymethod
     * @return
     */
    Orders createOrder(Integer[] cids, Integer aid, FrontUser frontUser, String expressmethod, String paymethod);

    /**
     * 根据订单id查询订单
     * @param orderid
     * @return
     */
    Orders QueryByOid(String orderid);

    /**
     *修改订单状态
     * @param out_trade_no
     * @param i
     */
    void updateOrderStatus(String out_trade_no, int i);
}
