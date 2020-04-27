package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Application extends BaseEntity{
   private Integer uid;
   private String phone;
   private String gname;
   //private String info;
   private Integer gnumber;
   private String reason;
   private Integer sqstatus;
   private String url;
}
