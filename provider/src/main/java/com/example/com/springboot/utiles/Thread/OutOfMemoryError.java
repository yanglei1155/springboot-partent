package com.example.com.springboot.utiles.Thread;

import org.apache.catalina.LifecycleState;

import java.util.ArrayList;
import java.util.List;

public class OutOfMemoryError {
    public static void main(String[] args) {
        List<String>list=new ArrayList<>();
        int i=0;
        while(true){
            list.add(String.valueOf(i++).intern());
        }
    }
}
