package com.example.com.springboot.service.impl;

import com.example.com.springboot.mapper.UserMapper;
import com.example.com.springboot.pojo.User;
import com.example.com.springboot.service.UserService;
import com.example.com.springboot.utiles.MD5Utils;
import org.apache.ibatis.annotations.Lang;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl extends BaseServcieImpl<UserMapper> implements UserService {

    @Override
    public User saveUser(User user) {
       String uid= UUID.randomUUID().toString().replaceAll("-","");
       user.setUid(uid);
       user.setUid("1");
       user.setPassword(MD5Utils.md5(user.getPassword()));
       mapper.saveUser(user);
       return user;
    }

    /**
     * 验证
     * @param user
     * @return
     */
    @Override
    public User checkUser(User user) {
        if(user.getPassword()!=""&&user.getPassword()!=null){
            user.setPassword(MD5Utils.md5(user.getPassword()));
        }
        User U = mapper.checkUser(user);
        return U;
    }

    @Override
    public User getUserHead(User user) {
         return  mapper.getUserHead(user);
    }
}
