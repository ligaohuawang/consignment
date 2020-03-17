package com.qf.service;

import com.qf.Base.BaseService;
import com.qf.entity.Address;

import java.util.List;

public interface IAddressService extends BaseService<Address> {
    /**
     * 根据用户id查询所有的地址信息
     * @param id
     * @return
     */
    List<Address> selectAllAddressByUid(Integer id);
}
