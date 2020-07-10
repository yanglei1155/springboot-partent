package com.example.com.springboot.utiles.Thread;

import java.util.concurrent.locks.LockSupport;

public class BaoZi  {
     public static  Object baozi=null;

    public static void main(String[] args) {
      // new BaoZi().bugBaoZiTest();
        //new BaoZi().deadTest();
       // new BaoZi().deadTest2();
       // new BaoZi().waitNotifyTest();
       // new BaoZi().waitNotifyDeadTest();
      //  new BaoZi().parkUnParkTest();
        new BaoZi().parkUnParkDeadTest();
    }

    public void bugBaoZiTest(){
        Thread thread=new Thread(()->{
           while (baozi==null){//如果没包子了就等，线程停止等待
               System.out.println("没包子了，等待包子生产出来");
               Thread.currentThread().suspend();//线程挂起
           }
           System.out.println("买到包子了，回家");
        });
        try {
            thread.start();
            Thread.sleep(2000L);
            baozi=new BaoZi();
            thread.resume();//唤醒线程
            System.out.println("通知顾客包子生产出来了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void deadTest(){//suspend 挂起线程不会释放锁当我生产者唤醒线程时需要获得该锁就会死锁
        Thread thread=new Thread(()->{
            while (baozi==null){//如果没包子了就等，线程停止等待
                System.out.println("没包子了，等待包子生产出来");
                synchronized (this){
                    Thread.currentThread().suspend();//线程挂起
                }

            }
            System.out.println("买到包子了，回家");
        });
        try {
            thread.start();
            Thread.sleep(2000L);
            baozi=new BaoZi();
            synchronized (this){
                thread.resume();//唤醒线程
             }

            System.out.println("通知顾客包子生产出来了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void deadTest2(){//当suspend后于resume也会产生死锁
        Thread thread=new Thread(()->{
            while (baozi==null){//如果没包子了就等，线程停止等待
                System.out.println("没包子了，等待包子生产出来");
                try {
                    Thread.sleep(5000L);//等待五秒后再线程挂起，让挂起操作就比生产者唤醒状态更慢
                    Thread.currentThread().suspend();//线程挂起
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
            System.out.println("买到包子了，回家");
        });
        try {
            thread.start();
            Thread.sleep(2000L);
            baozi=new BaoZi();
            synchronized (this){
                thread.resume();//唤醒线程
            }

            System.out.println("通知顾客包子生产出来了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitNotifyTest(){//当线程wait时会释放锁进入对象等待集合中，notify会唤醒对象等待集合中的线程
        Thread thread=new Thread(()->{
            while (baozi==null){//如果没包子了就等，线程停止等待
                System.out.println("没包子了，等待包子生产出来");
                synchronized (this){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("买到包子了，回家");
        });
        try {
            thread.start();
            Thread.sleep(2000L);
            baozi=new BaoZi();
            synchronized (this){
               this.notify();
            }

            System.out.println("通知顾客包子生产出来了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void waitNotifyDeadTest(){//当notify唤醒操作先于wait状态时就会死锁
        Thread thread=new Thread(()->{
            while (baozi==null){//如果没包子了就等，线程停止等待
                System.out.println("没包子了，等待包子生产出来");
                try {
                    Thread.sleep(5000L);
                    synchronized (this){
                            this.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("买到包子了，回家");
        });
        try {
            thread.start();
            Thread.sleep(2000L);
            baozi=new BaoZi();
            synchronized (this){
                this.notify();
            }

            System.out.println("通知顾客包子生产出来了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void parkUnParkTest(){//当线程被park当前线程挂起会等待unpark办法给我许可证我才会唤醒
        Thread thread=new Thread(()->{
            while (baozi==null){//如果没包子了就等，线程停止等待
                System.out.println("没包子了，等待包子生产出来");
                try {
                    Thread.sleep(5000L);
                    LockSupport.park();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("买到包子了，回家");
        });
        try {
            thread.start();
            Thread.sleep(2000L);
            baozi=new BaoZi();
            LockSupport.unpark(thread);//给指定颁发唤醒许可这里就不需要说是我先唤醒还是她先等待了
            System.out.println("通知顾客包子生产出来了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void parkUnParkDeadTest(){//当线程被park当前线程挂起但同suspend一样该线程不会释放锁
        Thread thread=new Thread(()->{
            while (baozi==null){//如果没包子了就等，线程停止等待
                System.out.println("没包子了，等待包子生产出来");
                  synchronized (this){
                      LockSupport.park();
                  }


            }
            System.out.println("买到包子了，回家");
        });
        try {
            thread.start();
            Thread.sleep(2000L);
            baozi=new BaoZi();
            synchronized (this){
                LockSupport.unpark(thread);
            }
            System.out.println("通知顾客包子生产出来了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
