package com.example.com.springboot.service.impl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseServcieImpl<T>{
    @Autowired
    protected T mapper;


}
