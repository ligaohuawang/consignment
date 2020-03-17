package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)

@TableName("order_details")
public class OrderDetails extends BaseEntity{
    private String oid;
    private Integer gid;
    private String subject;
    private String cover;
    private BigDecimal price;
    private Integer gnumber;
    private BigDecimal xiaoji;
    private Integer guid;
}
