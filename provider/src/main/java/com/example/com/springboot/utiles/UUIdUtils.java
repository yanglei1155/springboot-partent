package com.example.com.springboot.utiles;

import java.util.UUID;

public class UUIdUtils {
    public static String  getUUid(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return  uuid;
    }
}
