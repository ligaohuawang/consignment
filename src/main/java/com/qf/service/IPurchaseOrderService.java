package com.qf.service;

import com.qf.Base.BaseService;
import com.qf.entity.PurchaseOrder;

public interface IPurchaseOrderService extends BaseService<PurchaseOrder> {
    /**
     * 修改采购单状态
     * @param out_trade_no
     * @param i
     */
    void updateOrderStatus(String out_trade_no, int i);
}
