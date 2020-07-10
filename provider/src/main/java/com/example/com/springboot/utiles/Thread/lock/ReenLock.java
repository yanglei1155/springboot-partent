package com.example.com.springboot.utiles.Thread.lock;

import java.util.concurrent.locks.ReentrantLock;

public class ReenLock {
    private static final ReentrantLock lock=new ReentrantLock();
    public static void main(String[] args) {
        lock.lock();
        try {
            System.out.println("第一次获取锁，当前线程获取锁的次数"+lock.getHoldCount());
            lock.lock();
            System.out.println("第二次获取锁，当前线程获取锁的次数"+lock.getHoldCount());
        } finally {
            lock.unlock();
            lock.unlock();
        }
        System.out.println("当前线程获取锁的次数"+lock.getHoldCount());

        new Thread(()->{
            System.out.println(Thread.currentThread()+"期望获得锁");
           lock.lock();
           System.out.println(Thread.currentThread()+"获得了锁");
        }).start();
    }

}
