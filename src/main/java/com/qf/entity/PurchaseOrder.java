package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)

public class PurchaseOrder extends BaseEntity{
    private String oid;
    private Integer gid;
    private String subject;
    private String cover;
    private BigDecimal price;
    private Integer gnumber;
    private BigDecimal xiaoji;
    private String person;
    private String address;
    private String phone;
    private String code;
    private String expressmethod;
    private Integer puid;
    private Integer payStatus;
    private Integer deliver;
}
