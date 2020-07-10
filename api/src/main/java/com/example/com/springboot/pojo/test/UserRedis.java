package com.example.com.springboot.pojo.test;

import java.io.Serializable;

public class UserRedis  implements Serializable {
    private String name;
    private String sex;


    public UserRedis(String name, String sex){
        this.name=name;
        this.sex=sex;
    }
}
