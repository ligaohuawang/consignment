package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain =true)
public class ShopCart extends BaseEntity{
    private Integer uid;
    private Integer gid;
    private String gname;
    private BigDecimal gprice;
    private Integer gnumber;
    private BigDecimal xiaoji;
    private BigDecimal shiprice;
    @TableField(exist = false)
    private Goods goods;
}
