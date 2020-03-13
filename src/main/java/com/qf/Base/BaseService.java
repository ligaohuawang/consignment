package com.qf.Base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.ui.ModelMap;

import java.util.List;

//这是一个所有Service的公共的父接口
public interface BaseService<T> {
    int insert(T t);//1.添加对象
    int deleteById(Integer id);//根据id删除对象
    int update(T t);//3.根据id修改这个对象
    T selectById(Integer id);//4.根据id查询这个对象
    List<T> selectList();//5.查询全部，如果调用mapper的selectList()需要构造一个条件构造器
    ModelMap selectPage(Page<T> page, ModelMap map);//6.查询数据进行分页
}
