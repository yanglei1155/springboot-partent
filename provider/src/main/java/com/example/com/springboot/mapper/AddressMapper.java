package com.example.com.springboot.mapper;

import com.example.com.springboot.pojo.Address;
import com.example.com.springboot.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {
    void saveAddress(User user);

    List<Address> addressList(User user);

    Address getAddressById(Address address);
}
