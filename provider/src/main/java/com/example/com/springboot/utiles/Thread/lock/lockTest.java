package com.example.com.springboot.utiles.Thread.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class lockTest {
   private final YangLock lock=new YangLock();
   int n=0;
    public void  test(){
        lock.lock();
        try {
            n++;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws  Exception{
        lockTest lockTest=new lockTest();
        for(int j=0;j<3;j++){
            new Thread(()->{
                for(int i=0;i<10;i++){
                    lockTest.test();
                }
            }).start();
        }
        Thread.sleep(2000L);
        System.out.println(lockTest.n);
    }
}
