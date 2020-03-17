package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)

@TableName("goods_order")
public class Orders extends BaseEntity{
    private String oid;
    private Integer uid;
    private BigDecimal allprice;
    private String person;
    private String address;
    private String phone;
    private String code;
    private String paymethod;
    private String expressmethod;

    @TableField(exist = false)
    private List<OrderDetails> orderDetails;
}
