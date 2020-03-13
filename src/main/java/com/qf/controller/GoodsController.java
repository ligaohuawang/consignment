package com.qf.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/GoodsController")
public class GoodsController {
    @Autowired
    private IGoodsService iGoodsService;

    /**
     * 商品列表--查询未下架的所有商品
     * @param page
     * @param map
     * @return
     */
    @RequestMapping("/selectGoodsPage")
    public String selectGoodsPage(Page<Goods> page, ModelMap map){
        ModelMap map1 = iGoodsService.selectPage(page, map);
        map1.put("url","GoodsController/selectGoodsPage");
        map1.put("goodsList",page.getRecords());
        return "productList";
    }

    /**
     * 根据商品id查询商品详情
     * @param gid
     * @param map
     * @return
     */
    @RequestMapping("/productDetails")
    public String selectGoodsOne(Integer gid,ModelMap map){
        Goods goods = iGoodsService.selectById(gid);
        map.put("goodsDetatis",goods);
        return "productDetails";
    }
}
