package com.example.com.springboot.utiles.Thread.lock;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 信号量
 * 共享 但是有上限 限流
 */
public class SemaphoreTest {
    public static void main(String[] args){
        SemaphoreTest test=new SemaphoreTest();
        int N=8;
        NetSemaphor netSemaphor=new NetSemaphor(5);
        for(int i=0;i<N;i++){
            String vip="vip"+i;
            new Thread(()->{
                try {
                    netSemaphor.acquire();
                    test.service(vip);
                    netSemaphor.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

    public void service(String vip){
        try {
            System.err.println("欢迎贵宾"+vip);
            Thread.sleep(new Random().nextInt(3000));
            System.out.println("欢送贵宾"+vip);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
