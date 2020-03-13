package com.qf.service;

import com.qf.Base.BaseService;
import com.qf.entity.FrontUser;
import com.qf.entity.ShopCart;
import com.qf.result.ResultDate;
import java.util.List;

public interface IShopCartService extends BaseService<ShopCart> {
    /**
     * 添加购物车
     *
     * @param
     * @param gid
     * @param gnumber
     * @param cartToken
     * @return
     */
    String addCart(FrontUser frontUser, Integer gid, Integer gnumber, String cartToken);

    /**
     * 查询购物车列表
     * @param frontUser
     * @param cartToken
     * @return
     */
    List<ShopCart> shopCartList(FrontUser frontUser, String cartToken);

    /**
     * 购物车所有商品数量和价格之和
     * @param frontUser
     * @param cartToken
     * @return
     */
    ResultDate selectAllGoodsNumberAndPrice(FrontUser frontUser, String cartToken);

    /**
     * 去购物车结算
     * @param frontUser
     * @return
     */
    List<ShopCart> goCartCheck(FrontUser frontUser);

    /**
     * 添加购物车数量
     * @param id
     * @return
     */
    ResultDate addNumber(int id);

    /**
     * 减购物车数量
     * @param id
     * @return
     */
    ResultDate cutNumber(int id);
}
