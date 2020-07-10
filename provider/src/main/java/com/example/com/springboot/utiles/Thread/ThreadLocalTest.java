package com.example.com.springboot.utiles.Thread;

public class ThreadLocalTest {//ThreadLocal线程级别，每个线程会有自己归属的变量的值不受其它线程影响
    public static ThreadLocal<String>num=new ThreadLocal<>();

    public static void main(String[] args) throws  Exception{
        System.out.println("主线程未设置值之前:"+num.get());
        num.set("123");
        System.out.println("主线程设置完值后："+num.get());
        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
              System.out.println("线程1未设置值前:"+num.get());
              num.set("255");
              System.out.println("线程1设置完值后："+num.get());
            }
        });
        thread1.start();
    }
}
