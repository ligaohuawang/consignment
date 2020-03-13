package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qf.entity.Goods;
import com.qf.entity.GoodsImages;
import com.qf.mapper.IGoodsImagesMapper;
import com.qf.mapper.IGoodsMapper;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;
@Service
public class IGoodsServiceImpl implements IGoodsService {
    @Autowired
    private IGoodsMapper iGoodsMapper;
    @Autowired
    private IGoodsImagesMapper iGoodsImagesMapper;

    @Override
    public int insert(Goods goods) {
        return iGoodsMapper.insert(goods);
    }

    @Override
    public int deleteById(Integer id) {
        return iGoodsMapper.deleteById(id);
    }

    @Override
    public int update(Goods goods) {
        return iGoodsMapper.update(goods,null);
    }

    @Override
    public Goods selectById(Integer id) {
        Goods goods = iGoodsMapper.selectById(id);
        //这里还有去图片表将图片查询出来
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.eq("gid",id);
        List<GoodsImages> goodsImagesList = iGoodsImagesMapper.selectList(queryWrapper);
        if (goodsImagesList!=null){//如果图片不为空
            for (int i = 0; i <goodsImagesList.size() ; i++) {
                if (goodsImagesList.get(i).getIscover()==1){
                    //如果是封面
                    goods.setFengmianurl(goodsImagesList.get(i).getUrl());
                }else {
                    //如果不是封面
                    List<String> otherUrlList = new ArrayList<>();
                    otherUrlList.add(goodsImagesList.get(i).getUrl());
                    //赋值入Goods的List<string> otherUrls
                    goods.setOtherurls(otherUrlList);
                }
            }
        }
        return goods;
    }

    @Override
    public List<Goods> selectList() {
        return iGoodsMapper.selectList(null);
    }

    @Override
    public ModelMap selectPage(Page<Goods> page, ModelMap map) {
        //查询未被下架的商品
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("status",1);
        IPage<Goods> iPage = iGoodsMapper.selectPage(page, queryWrapper1);
        page.setRecords( iPage.getRecords());
        for (int i=0;i<page.getRecords().size();i++){
            //拿到每一个商品id
            Integer id = page.getRecords().get(i).getId();
            //根据商品id将商品封面和其它图片的图片集合查询出来
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("gid",id);
            List<GoodsImages> list = iGoodsImagesMapper.selectList(queryWrapper2);
            //判断每一张图片是否是封面
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getIscover()==1){
                //是封面,将封面地址放入对象中
                    page.getRecords().get(i).setFengmianurl(list.get(j).getUrl());
                }else {
                //不是封面，将图片信息放入对象的集合中
                    List<String> otherurls = page.getRecords().get(i).getOtherurls();
                    //otherurls.add(list.get(j).getUrl());
                    if (otherurls==null) {
                        List<String> otherurls2 = new ArrayList<>();
                        otherurls2.add(list.get(j).getUrl());
                        //设置回对象中
                        page.getRecords().get(i).setOtherurls(otherurls2);
                    }else {
                        otherurls.add(list.get(j).getUrl());
                        //设置回对象中
                        page.getRecords().get(i).setOtherurls(otherurls);
                    }
                }
            }

        }
        //将对象put入map中
        map.put("page",page);
        return map;
    }
}
