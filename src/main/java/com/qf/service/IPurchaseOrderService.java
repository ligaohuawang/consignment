package com.qf.service;

import com.qf.Base.BaseService;
import com.qf.entity.Application;
import com.qf.entity.PurchaseOrder;
import com.qf.result.ResultDate;

import java.util.List;

public interface IPurchaseOrderService extends BaseService<PurchaseOrder> {
    /**
     * 修改采购单状态
     * @param out_trade_no
     * @param i
     */
    void updateOrderStatus(String out_trade_no, int i);

    /**
     * 提交代销申请
     * @param application
     */
    void addApplication(Application application);

    /**
     * 查询“我”的代销申请
     * @param id
     * @return
     */
    List<Application> myApplyForSell(Integer id);

    /**
     * 根据用户id查询他自己的采购订单
     * @param id
     * @return
     */
    List<PurchaseOrder> myPurchaseOrders(Integer id);

    /**
     * 发货
     * @param id
     * @return
     */
    ResultDate fahuo(Integer id);
}
