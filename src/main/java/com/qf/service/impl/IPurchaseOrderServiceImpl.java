package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qf.entity.PurchaseOrder;
import com.qf.mapper.IPurchaseOrderMapper;
import com.qf.service.IPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
@Service
public class IPurchaseOrderServiceImpl implements IPurchaseOrderService {
    @Autowired
    private IPurchaseOrderMapper iPurchaseOrderMapper;

    @Override
    public int insert(PurchaseOrder purchaseOrder) {
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

    @Override
    public int update(PurchaseOrder purchaseOrder) {
        return 0;
    }

    @Override
    public PurchaseOrder selectById(Integer id) {
        return null;
    }

    @Override
    public List<PurchaseOrder> selectList() {
        return null;
    }

    @Override
    public ModelMap selectPage(Page<PurchaseOrder> page, ModelMap map) {
        return null;
    }

    /**
     * 修改采购单状态
     * @param out_trade_no
     * @param i
     */
    @Override
    public void updateOrderStatus(String out_trade_no, int i) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("oid",out_trade_no);
        List<PurchaseOrder> purchaseOrderList = iPurchaseOrderMapper.selectList(queryWrapper);
        for (int i1 = 0; i1 < purchaseOrderList.size(); i1++) {
            PurchaseOrder purchaseOrder = purchaseOrderList.get(i1);
            purchaseOrder.setStatus(i);
            iPurchaseOrderMapper.updateById(purchaseOrder);
        }
    }
}
