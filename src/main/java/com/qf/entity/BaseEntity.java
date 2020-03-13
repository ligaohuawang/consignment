package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor//全参
@NoArgsConstructor//无参
@Accessors(chain = true)//链式编程
public class BaseEntity implements Serializable {
    @TableId(type = IdType.AUTO)//主键自动递增
    private Integer id;
    private Date createTime;
    private Integer status;
}
