package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qf.entity.Application;
import com.qf.entity.PurchaseOrder;
import com.qf.mapper.IApplicationMapper;
import com.qf.mapper.IPurchaseOrderMapper;
import com.qf.result.ResultDate;
import com.qf.service.IPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.Date;
import java.util.List;
@Service
public class IPurchaseOrderServiceImpl implements IPurchaseOrderService {
    @Autowired
    private IPurchaseOrderMapper iPurchaseOrderMapper;
    @Autowired
    private IApplicationMapper iApplicationMapper;

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
            purchaseOrder.setPayStatus(i);
            iPurchaseOrderMapper.updateById(purchaseOrder);
        }
    }

    /**
     * 提交代销申请
     * @param application
     */
    @Override
    public void addApplication(Application application) {
        application.setCreateTime(new Date());
        application.setStatus(0);
        application.setSqstatus(0);
        iApplicationMapper.insert(application);
    }

    /**
     * 查询“我”的代销申请
     * @param id
     * @return
     */
    @Override
    public List<Application> myApplyForSell(Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",id);
        List<Application> applicationList = iApplicationMapper.selectList(queryWrapper);
        return applicationList;
    }

    /**
     * 根据用户id查询他自己的采购订单
     * @param id
     * @return
     */
    @Override
    public List<PurchaseOrder> myPurchaseOrders(Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("puid",id);
        List<PurchaseOrder> purchaseOrderList = iPurchaseOrderMapper.selectList(queryWrapper);
        return purchaseOrderList;
    }

    /**
     * 发货
     * @param id
     * @return
     */
    @Override
    public ResultDate fahuo(Integer id) {
        QueryWrapper queryWrapper = new  QueryWrapper();
        queryWrapper.eq("id",id);

        PurchaseOrder purchaseOrder = iPurchaseOrderMapper.selectOne(queryWrapper);
        purchaseOrder.setDeliver(1);

        int update = iPurchaseOrderMapper.update(purchaseOrder, queryWrapper);
        if (update>0){
          return new ResultDate().setResult(true);
        }

        return new ResultDate().setResult(false);
    }

}
