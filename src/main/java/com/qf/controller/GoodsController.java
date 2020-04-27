package com.qf.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qf.entity.Goods;
import com.qf.result.ResultData;
import com.qf.service.IGoodsService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@Controller
@RequestMapping("/GoodsController")
public class GoodsController {
    @Autowired
    private IGoodsService iGoodsService;

    //准备一个本地磁盘的路径
    private String localpath = "F:/bysj/images";


    /**
     * 上传图片到本地磁盘
     * @param file
     * @return
     */
    @RequestMapping("/shangchuan")
    @ResponseBody
    public ResultData<String> shangchuan(MultipartFile file) {
        //输出流必须写到文件名，所以先得到一个文件名
        String fileName = UUID.randomUUID().toString()+file.getOriginalFilename();

        //上传的真实路径
        String ShangChuanRealpath = localpath + "/" + fileName;
        try(
                //从file得到一个输入流
                InputStream is = file.getInputStream();
                //构造一个输出流
                OutputStream out = new FileOutputStream(ShangChuanRealpath);
        ) {
            IOUtils.copy(is,out);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultData<String>().setCode(ResultData.ResultCodeList.OK).setData(ShangChuanRealpath);
    }


    /**
     * 商品列表--查询未下架的所有商品
     * @param page
     * @param map
     * @return
     */

    //TODO L1 controller接收用户点击“产品列表”的请求前去查询产品列表
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
        map.put("urlList",goods.getOtherurls());
        return "productDetails";
    }

    //TODO A3 获得img标签发过来的图片路径去读取图片并返回给img标签（封面）（多张图片）
    @RequestMapping("queryImageByServer")
    public void queryImageByServer(String ShangChuanRealpath, HttpServletResponse response){
        try (
                InputStream in = new FileInputStream(ShangChuanRealpath);
                ServletOutputStream out = response.getOutputStream();
        ){
            IOUtils.copy(in, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
