package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qf.entity.*;
import com.qf.mapper.IGoodsImagesMapper;
import com.qf.mapper.IOrderDetailsMapper;
import com.qf.mapper.IOrdersMapper;
import com.qf.mapper.IPurchaseOrderMapper;
import com.qf.service.IAddressService;
import com.qf.service.IGoodsService;
import com.qf.service.IOrderService;
import com.qf.service.IShopCartService;
import com.qf.utils.PriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class IOrderServiceImpl implements IOrderService {
    @Autowired
    private IOrdersMapper iOrdersMapper;
    @Autowired
    private IAddressService iAddressService;
    @Autowired
    private IShopCartService iShopCartService;
    @Autowired
    private IOrderDetailsMapper iOrderDetailsMapper;
    @Autowired
    private IGoodsService iGoodsService;
    @Autowired
    private IGoodsImagesMapper iGoodsImagesMapper;
    @Autowired
    private IPurchaseOrderMapper iPurchaseOrderMapper;

    @Override
    public int insert(Orders orders) {
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

    @Override
    public int update(Orders orders) {
        return 0;
    }

    @Override
    public Orders selectById(Integer id) {
        return null;
    }

    @Override
    public List<Orders> selectList() {
        return null;
    }

    @Override
    public ModelMap selectPage(Page<Orders> page, ModelMap map) {
        return null;
    }

    /**
     * 下单
     * @param cids
     * @param aid
     * @param frontUser
     * @param expressmethod
     * @param paymethod
     * @return
     */
    @Override
    @Transactional
    public Orders createOrder(Integer[] cids, Integer aid, FrontUser frontUser, String expressmethod, String paymethod) {
        //1.根据地址id查询出地址
        Address address = iAddressService.selectById(aid);
        //2.计算订单总价
        //2.1先获得要生成订单的购物车清单
        List<ShopCart> shopCartList = iShopCartService.selectListByCids(cids);
        //2.2计算总价
        double allPrice = PriceUtil.allPrice(shopCartList);
        //创建订单
        Orders orders = new Orders();
        orders.setCreateTime(new Date());
        orders.setStatus(0);
        orders.setOid(UUID.randomUUID().toString());
        orders.setUid(frontUser.getId());
        orders.setAllprice(BigDecimal.valueOf(allPrice));
        orders.setPerson(address.getPerson());
        orders.setAddress(address.getAddress());
        orders.setPhone(address.getPhone());
        orders.setCode(address.getCode());
        orders.setExpressmethod(expressmethod);
        orders.setPaymethod(paymethod);
        //将订单添加入订单表
        int insert = iOrdersMapper.insert(orders);

        //构建并保存订单详情
        for (ShopCart shopCart : shopCartList) {
            //根据购物车id得商品id
            //根据商品id查询得商品标题，商品封面，商品价格，商品所属用户
            Goods goods = iGoodsService.selectById(shopCart.getGid());
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("gid",shopCart.getGid());
            queryWrapper.eq("iscover",1);
            GoodsImages goodsImages = iGoodsImagesMapper.selectOne(queryWrapper);
            OrderDetails orderDetails = new OrderDetails()
           .setOid(orders.getOid())
                    .setGid(shopCart.getGid())
                    .setSubject(goods.getSubject())
                    .setCover(goodsImages.getUrl())
                    .setPrice(goods.getPrice())
                    .setGnumber(shopCart.getGnumber())
                    .setXiaoji(shopCart.getXiaoji())
                    .setGuid(goods.getUid());
            orderDetails.setStatus(1);
            orderDetails.setCreateTime(new Date());
            //添加订单详情
            int insert1 = iOrderDetailsMapper.insert(orderDetails);

            //删除购物车清单
            int i = iShopCartService.deleteByCids(cids);
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setCreateTime(new Date())
                    .setStatus(0);
            purchaseOrder.setOid(orders.getOid());
            purchaseOrder.setGid(shopCart.getGid());
            purchaseOrder.setSubject(goods.getSubject());
            purchaseOrder.setCover(goodsImages.getUrl());
            purchaseOrder.setPrice(goods.getPrice());
            purchaseOrder.setGnumber(shopCart.getGnumber());
            purchaseOrder.setXiaoji(shopCart.getXiaoji());
            purchaseOrder.setPerson(address.getPerson());
            purchaseOrder.setAddress(address.getAddress());
            purchaseOrder.setPhone(address.getPhone());
            purchaseOrder.setExpressmethod(expressmethod);
            purchaseOrder.setCode(address.getCode());
            purchaseOrder.setPuid(goods.getUid());
            //添加采购单
            int insert2 = iPurchaseOrderMapper.insert(purchaseOrder);
        }
        return orders;
    }

    /**
     * 根据订单id查询订单
     * @param orderid
     * @return
     */
    @Override
    public Orders QueryByOid(String orderid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("oid",orderid);
        Orders orders = iOrdersMapper.selectOne(queryWrapper);


        //查询订单详情
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("oid", orders.getOid());
        List<OrderDetails> list = iOrderDetailsMapper.selectList(queryWrapper2);
        orders.setOrderDetails(list);

        return orders;
    }

    /**
     * 修改订单状态
     * @param out_trade_no
     * @param i
     */
    @Override
    public void updateOrderStatus(String out_trade_no, int i) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("oid",out_trade_no);
        Orders orders = iOrdersMapper.selectOne(queryWrapper);
        orders.setPayStatus(i);
        iOrdersMapper.updateById(orders);
    }

    /**
     * 查询用户订单
     * @param id
     * @return
     */
    @Override
    public List<Orders> selectUserOrder(Integer id) {
        //先根据id查询出订单集合
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",id);
        List<Orders> ordersList = iOrdersMapper.selectList(queryWrapper);
        //遍历订单集合查询订单详情
        if (ordersList.size()>0){
            for (int i = 0; i < ordersList.size(); i++) {
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("oid",ordersList.get(i).getOid());
                List<OrderDetails> orderDetailsList = iOrderDetailsMapper.selectList(queryWrapper1);
                ordersList.get(i).setOrderDetails(orderDetailsList);
            }
            return ordersList;
        }
        return null;
    }
}
