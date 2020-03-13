package com.qf.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResultDate<T> {
    private boolean result;//操作是否成功,true,成功,false，不成功
    private String data;//提示信息
    private String returnUrl;//登录成功后返回的路径
    private T resultDate;//用来保存对象信息，比如用户对象

    private BigDecimal xiaoji;//用来保存xiaoji
}
