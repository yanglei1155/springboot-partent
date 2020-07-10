package com.example.com.springboot.utiles.Thread.lock;

public class ReadWriteLock {
    final static ReadWriteLock readWriteLock=new ReadWriteLock();
    public static void main(String[] args) {//读操作存在同步这样就不符合我们需求，我们允许同时读，但是不允许边读边写
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

    public  synchronized  void read(Thread thread){
       Long start= System.currentTimeMillis();
       while (System.currentTimeMillis()-start<1){
           System.out.println(thread.getName()+"正在读操作");
       }
    }
    public  synchronized  void write(Thread thread){
        Long start= System.currentTimeMillis();
        while (System.currentTimeMillis()-start<1){
            System.out.println(thread.getName()+"正在写操作");
        }
    }
}
