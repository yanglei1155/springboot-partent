package com.example.com.springboot.service.impl;

import com.example.com.springboot.mapper.AddressMapper;
import com.example.com.springboot.pojo.Address;
import com.example.com.springboot.pojo.User;
import com.example.com.springboot.pojo.test.UserRedis;
import com.example.com.springboot.service.AddressService;
import com.example.com.springboot.service.UserService;
import com.example.com.springboot.utiles.UUIdUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceImpl extends BaseServcieImpl<AddressMapper>implements AddressService {
    @Override
    public void saveAddress(User user) {
        user.setAddressId(UUIdUtils.getUUid());
        user.setStatus("1");
        mapper.saveAddress(user);
    }

    @Override
    public List<Address> addressList(User user) {

        return mapper.addressList(user);
    }

    @Override
    public Address getAddressById(Address address) {
        return  mapper.getAddressById(address);
    }

    @Override
    @Cacheable(cacheManager ="cacheManager",value = "mycatch",key = "#name")
    public UserRedis getUser(String name) {
        return  new UserRedis(name,"20");
    }

}
