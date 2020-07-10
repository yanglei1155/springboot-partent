package com.example.com.springboot.mapper;

import com.example.com.springboot.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void saveUser(User user);

    User checkUser(User user);

    User getUserHead(User user);
}
