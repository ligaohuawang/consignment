package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qf.entity.Address;
import com.qf.mapper.IAddressMapper;
import com.qf.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;

@Service
public class IAddressServiceImpl implements IAddressService {
    @Autowired
    private IAddressMapper iAddressMapper;

    @Override
    public int insert(Address address) {
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

    @Override
    public int update(Address address) {
        return 0;
    }

    @Override
    public Address selectById(Integer id) {
        return iAddressMapper.selectById(id);
    }

    @Override
    public List<Address> selectList() {
        return null;
    }

    @Override
    public ModelMap selectPage(Page<Address> page, ModelMap map) {
        return null;
    }

    /**
     * 根据用户id查询所有的地址信息
     * @param id
     * @return
     */
    @Override
    public List<Address> selectAllAddressByUid(Integer id) {
        //根据用户id查询用户地址
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", id);
        List<Address> addressList = iAddressMapper.selectList(queryWrapper);
        return addressList;
    }
}
