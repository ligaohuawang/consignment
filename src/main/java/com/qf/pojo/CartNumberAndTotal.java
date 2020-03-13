package com.qf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CartNumberAndTotal {
    private Integer number;
    private BigDecimal zongjia;
}
