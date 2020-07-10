package com.example.com.springboot.service;

import com.example.com.springboot.pojo.User;

public interface UserService {
    User saveUser(User user);

    User checkUser(User user);

     User  getUserHead(User user);
}
