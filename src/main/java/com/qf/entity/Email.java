package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Email implements Serializable {
    //1.发送给谁
    private String to;
    //2.标题
    private String subject;
    //3.内容
    private String context;
    //4.发送时间
    private Date sendTime;
}
