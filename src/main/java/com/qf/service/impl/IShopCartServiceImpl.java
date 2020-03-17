package com.qf.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qf.entity.FrontUser;
import com.qf.entity.Goods;
import com.qf.entity.ShopCart;
import com.qf.mapper.IShopCartMapper;
import com.qf.pojo.CartNumberAndTotal;
import com.qf.result.ResultDate;
import com.qf.service.IGoodsService;
import com.qf.service.IShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class IShopCartServiceImpl implements IShopCartService {
    @Autowired
    private IShopCartMapper iShopCartMapper;
    @Autowired
    private IGoodsService iGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public int insert(ShopCart shopCart) {
        return iShopCartMapper.insert(shopCart);
    }

    @Override
    public int deleteById(Integer id) {
        return iShopCartMapper.deleteById(id);
    }

    @Override
    public int update(ShopCart shopCart) {
        return iShopCartMapper.update(shopCart,null);
    }

    @Override
    public ShopCart selectById(Integer id) {
        return iShopCartMapper.selectById(id);
    }

    @Override
    public List<ShopCart> selectList() {
        return iShopCartMapper.selectList(null);
    }

    @Override
    public ModelMap selectPage(Page<ShopCart> page, ModelMap map) {
        return null;
    }

    /**
     * 添加购物车
     * @param frontUser
     * @param gid
     * @param gnumber
     * @param cartToken
     * @return
     */
    @Override
    public String addCart(FrontUser frontUser, Integer gid, Integer gnumber, String cartToken) {
        //根据gid查询商品信息
        Goods good = iGoodsService.selectById(gid);
        BigDecimal cartPrice = good.getPrice().multiply(BigDecimal.valueOf(gnumber));
        //构造一个cart对象（gid,gnumber,小计,uid）
        ShopCart cart = new ShopCart();
        cart.setGid(gid).setGname(good.getSubject()).setGprice(good.getPrice()).setGnumber(gnumber).setXiaoji(cartPrice).setStatus(1).setCreateTime(new Date());
        //1.判断用户是否为空
        //1.1为空（没有登录）
        //1.1.1判断cartToken是否为空
        //为空，创建一个uuid作为cartToken
        //不为空
        //1.1.2根据cartToken将cart从左push入链表
        //1.2不为空
        //1.2.1调用mybatis的单表操作方法将cart添加入数据库
        if (null != frontUser) {
            cart.setUid(frontUser.getId());
            iShopCartMapper.insert(cart);
        } else {
            if (null == cartToken) {
                cartToken = UUID.randomUUID().toString();
            }
            redisTemplate.opsForList().leftPush(cartToken, cart);
        }
        return cartToken;
    }

    /**
     * 查询购物车列表
     * @param frontUser
     * @param cartToken
     * @return
     */
    @Override
    public List<ShopCart> shopCartList(FrontUser frontUser, String cartToken) {
        //1.判断用户是否登录
        //1.1为空：未登录
        //根据cartToken购物车唯一标识查询redis临时数据库
        //1.2不为空：已登录
        //根据用户id直接查询数据库
        //2.遍历购物车中商品，查询每一件商品的详细信息
        List<ShopCart> shopCartList = null;
        if (null == frontUser) {
            //查询redis
            if (cartToken != null) {
                //获得redis中购物车的长度
                Long size = redisTemplate.opsForList().size(cartToken);
                shopCartList = redisTemplate.opsForList().range(cartToken, 0, size);
            }
        } else {
            //直接查询数据库
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid", frontUser.getId());
            queryWrapper.orderByDesc("create_time");
            shopCartList = iShopCartMapper.selectList(queryWrapper);
        }
        //如果购物车不为空
        if (null != shopCartList) {
            //遍历购物车查询购物车中每一件商品
            for (ShopCart car : shopCartList) {
                //得到商品id
                Integer gid = car.getGid();
                //查询商品信息
                Goods goods = iGoodsService.selectById(gid);
                System.out.println("查询到的商品" + goods);
                car.setGoods(goods);
            }

        }

        return shopCartList;
    }

    /**
     * 购物车所有商品数量和价格之和
     * @param frontUser
     * @param cartToken
     * @return
     */
    @Override
    public ResultDate selectAllGoodsNumberAndPrice(FrontUser frontUser, String cartToken) {
        List<ShopCart> shopCartList = null;
        if (null == frontUser) {
            //查询redis
            if (cartToken != null) {
                //获得redis中购物车的长度
                Long size = redisTemplate.opsForList().size(cartToken);
                shopCartList = redisTemplate.opsForList().range(cartToken, 0, size);
            }
        } else {
            //直接查询数据库
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid", frontUser.getId());
            queryWrapper.orderByDesc("create_time");
            shopCartList = iShopCartMapper.selectList(queryWrapper);
        }

        //如果购物车不为空
        if (null != shopCartList) {
            //遍历购物车查询购物车中每一件商品
            Integer gnumber=0;
            BigDecimal zongjia = new BigDecimal("0.00");
            for (ShopCart car : shopCartList) {
                //计算购物车中商品总数
                gnumber=gnumber+car.getGnumber();
                //计算购物车总价
                BigDecimal xiaoji = car.getGprice().multiply(BigDecimal.valueOf(gnumber));
                zongjia= zongjia.add(xiaoji);
            }
            CartNumberAndTotal cartNumberAndTotal = new CartNumberAndTotal();
            cartNumberAndTotal.setNumber(gnumber).setZongjia(zongjia);
            return new ResultDate().setResult(true).setResultDate(cartNumberAndTotal);
        }
        return new ResultDate().setResult(false);
    }

    /**
     * 去购物车结算
     * @param frontUser
     * @return
     */
    @Override
    public List<ShopCart> goCartCheck(FrontUser frontUser) {
        List<ShopCart> shopCartList = null;
        if(frontUser!=null){
            //直接查询数据库
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid", frontUser.getId());
            queryWrapper.orderByDesc("create_time");
            shopCartList = iShopCartMapper.selectList(queryWrapper);

            //如果购物车不为空
            if (null != shopCartList) {
                //遍历购物车查询购物车中每一件商品
                for (ShopCart car : shopCartList) {
                    //得到商品id
                    Integer gid = car.getGid();
                    //查询商品信息
                    Goods goods = iGoodsService.selectById(gid);
                    System.out.println("查询到的商品" + goods);
                    car.setGoods(goods);
                }
            }
        }
        return shopCartList;
    }

    /**
     * 添加购物车商品数量
     * @param id
     * @return
     */
    @Override
    public ResultDate addNumber(int id) {
        ShopCart shopCart = iShopCartMapper.selectById(id);
        shopCart.setGnumber(shopCart.getGnumber()+1);
        shopCart.setXiaoji(shopCart.getGprice().multiply(BigDecimal.valueOf(shopCart.getGnumber())));

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",id);
        iShopCartMapper.update(shopCart,queryWrapper);
        ResultDate resultDate = new ResultDate();
        resultDate.setResult(true).setXiaoji(shopCart.getXiaoji());
        return resultDate;
    }

    /**
     * 减少购物车商品数量
     * @param id
     * @return
     */
    @Override
    public ResultDate cutNumber(int id) {
        return null;
    }

    /**
     * 根据商品id和用户id将购物车查询出来
     * @param checkitems
     * @param frontUser
     * @return
     */
    @Override
    public List<ShopCart> queryCartsByGidAndUid(Integer[] checkitems, FrontUser frontUser) {
        //1.创建条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", frontUser.getId());
        //商品id在这个数组里面的话就会被查询出来
        queryWrapper.in("id", checkitems);
        List<ShopCart> shopCartList = iShopCartMapper.selectList(queryWrapper);

        for (ShopCart shopCart : shopCartList) {
            Integer id = shopCart.getGid();
            Goods goods =iGoodsService.selectById(id);
            shopCart.setGoods(goods);
        }
        return shopCartList;
    }

    /**
     * 根据商品购物车id查询购物车信息
     * @param cids
     * @return
     */
    @Override
    public List<ShopCart> selectListByCids(Integer[] cids) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("id",cids);
        List<ShopCart> shopCartList = iShopCartMapper.selectList(queryWrapper);
        return shopCartList;
    }

    /**
     * 删除购物车清单
     * @param cids
     * @return
     */
    @Override
    public int deleteByCids(Integer[] cids) {
        int i = iShopCartMapper.deleteBatchIds(Arrays.asList(cids));
        return i;
    }
}
