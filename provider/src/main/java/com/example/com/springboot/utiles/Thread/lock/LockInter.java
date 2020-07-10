package com.example.com.springboot.utiles.Thread.lock;

import java.util.concurrent.locks.ReentrantLock;

public class LockInter{
    private static  final ReentrantLock lock=new ReentrantLock();
   static Runnable runnable=new Runnable(){
        @Override
        public void run()  {
            try {
                test();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    public static void main(String[] args) throws  Exception{
        Thread thread1=new Thread(runnable);
        Thread thread2=new Thread(runnable);
        thread1.start();
        Thread.sleep(500);
        thread2.start();
        Thread.sleep(2000);
        thread2.interrupt();
    }
    public static void test() throws  InterruptedException{
        System.out.println(Thread.currentThread().getName()+"尝试获取锁");
        lock.lockInterruptibly();
        try{
            System.out.println(Thread.currentThread().getName()+"获得了锁");
            Thread.sleep(10000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            System.out.println(Thread.currentThread().getName()+"执行了finally方法");
            lock.unlock();
            System.out.println(Thread.currentThread().getName()+"释放了锁");
        }
    }

}
