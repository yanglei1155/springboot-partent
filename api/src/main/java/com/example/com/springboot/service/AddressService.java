package com.example.com.springboot.service;

import com.example.com.springboot.pojo.Address;
import com.example.com.springboot.pojo.User;
import com.example.com.springboot.pojo.test.UserRedis;

import java.util.List;

public interface AddressService {

    void saveAddress(User user);

    List<Address>addressList(User user);

    Address getAddressById(Address address);

     UserRedis getUser(String name);
}
