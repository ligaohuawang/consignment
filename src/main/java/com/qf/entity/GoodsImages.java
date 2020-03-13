package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GoodsImages extends BaseEntity implements Serializable{
    //图片路径
    private String url;
    //图片描述
    private String info;
    //是否为封面，1-封面，0-非封面
    private Integer iscover;
    //商品id
    private Integer gid;
}
