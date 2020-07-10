package com.example.com.springboot.utiles.Thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLock2 {
    ReentrantReadWriteLock r=new ReentrantReadWriteLock();
    final static ReadWriteLock readWriteLock=new ReadWriteLock();
    public static void main(String[] args) {
        new Thread(()->{
            readWriteLock.read(Thread.currentThread());
        }).start();
        new Thread(()->{
            readWriteLock.read(Thread.currentThread());
        }).start();
        new Thread(()->{
            readWriteLock.write(Thread.currentThread());
        }).start();
    }

    public void read(Thread thread){
        r.readLock().lock();
        try {
            Long start= System.currentTimeMillis();
            while (System.currentTimeMillis()-start<=2){
                System.out.println(thread.getName()+"正在读操作");
            }
            System.out.println(thread.getName()+"读操作完毕");
        }finally {
            r.readLock().unlock();
        }
    }
    public void write(Thread thread){
        r.writeLock().lock();
        try {
            Long start= System.currentTimeMillis();
            while (System.currentTimeMillis()-start<=2){
                System.out.println(thread.getName()+"正在写操作");
            }
            System.out.println(thread.getName()+"写操作完毕");
        }finally {
            r.writeLock().unlock();
        }
    }
}
