package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Goods extends BaseEntity implements Serializable {
    //标题
    private String subject;
    //描述
    private String info;
    //价格
    private BigDecimal price;
    //库存
    private Integer save;
    //所属分类
    private Integer tid;
    //所属用户
    private Integer uid;
    //市场价
    private BigDecimal shiprice;
    //封面图片的路径
    @TableField(exist = false)
    private String fengmianurl;
    //其它图片的路径
    @TableField(exist = false)
    private List<String> otherurls;
}
