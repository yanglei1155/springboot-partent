package com.example.com.springboot.utiles.Thread;

import sun.nio.ch.ThreadPool;

import java.util.List;
import java.util.concurrent.*;

public class ThreaPool {
    public static void main(String[] args) throws  Exception {
      new ThreaPool().threadPoolTest1();
      //  new ThreaPool().threadPoolTest2();
      //  new ThreaPool().threadPoolTest3();
       // new ThreaPool().threadPoolTest4();
     //   new ThreaPool().threadPoolTest5();
       // new ThreaPool().threadPoolTest7();
        //new ThreaPool().threadPoolTest8();
    }
    void testCommon(ThreadPoolExecutor poolExecutor){
        for(int i=0;i<15;i++){//向线程池内提交15个任务
            int n=i;
            poolExecutor.submit(new Runnable() {
                @Override
                public void run() {//每个线程具体做的东西(并行执行哦，理论上最少需要9秒钟)
                    try {
                        System.out.println("执行了"+Thread.currentThread().getName());
                        Thread.sleep(3000L);
                        System.err.println("结束了"+Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("提交第"+i+"任务");
        }
        try {
            Thread.sleep(500L);
            System.out.println("线程池大小"+poolExecutor.getPoolSize());
            System.out.println("线程队列大小"+poolExecutor.getQueue().size());
            Thread.sleep(9000L);
            System.out.println("线程池大小"+poolExecutor.getPoolSize());
            System.out.println("线程队列大小"+poolExecutor.getQueue().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void  threadPoolTest1() throws  Exception{//5个核心线程，最大10，超过核心线程5秒内没做任务就自动销毁
        ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(5,10,5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        testCommon(poolExecutor);
    }

    public void threadPoolTest2() throws  Exception{//5个核心，最大10个，5秒后超过核心的没使用就自动销毁，队列最大3，超过的拒绝执行
      ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
              new LinkedBlockingQueue<Runnable>(3), new RejectedExecutionHandler() {
          @Override
          public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
              System.err.println("有任务被拒绝执行");
          }
      });
      testCommon(poolExecutor);

    }

    public void threadPoolTest3()throws  Exception{//核心线程0个超出30秒无任务就销毁线程，最大为MAX_VALUE，
        //如果此时线程池没有空闲线程来完成任务，SYnchronousQueue会拒绝将任务放入队列
        //此时线程池会创建一个线程来完成被拒绝的任务
        ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(0,Integer.MAX_VALUE,30,TimeUnit.SECONDS,
                new SynchronousQueue<>());
        testCommon(poolExecutor);
        Thread.sleep(30000L);
        System.out.println("30秒后此时线程池大小："+poolExecutor.getPoolSize());
    }

    public void threadPoolTest4()throws  Exception{//定时任务，核心线程数为5，3秒后执行任务延迟队列中的任务
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor=new ScheduledThreadPoolExecutor(5);
        scheduledThreadPoolExecutor.schedule(new Runnable() {
            @Override
            public void run() {
               System.out.println("当前执行任务时间为:"+System.currentTimeMillis());
            }
        },3000,TimeUnit.MILLISECONDS);
       System.out.println("任务提交："+System.currentTimeMillis()+"当前线程池线程个数为:"+scheduledThreadPoolExecutor.getPoolSize());
    }

    public void threadPoolTest5()throws  Exception{
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor=new ScheduledThreadPoolExecutor(5);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                     System.out.println("任务1执行时间为"+System.currentTimeMillis());
                } catch (InterruptedException e) {


                }
            }
        },2000,1000,TimeUnit.MILLISECONDS);
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    System.out.println("任务2执行时间为"+System.currentTimeMillis());
                } catch (InterruptedException e) {


                }
            }
        },2000,1000,TimeUnit.MILLISECONDS);
    }

    public void threadPoolTest7(){
        ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(3), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.err.println("任务被拒绝执行");
            }
        });
        for(int i=0;i<15;i++){
            int n=i;
            poolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("线程："+Thread.currentThread().getName()+"在执行任务"+n);
                        Thread.sleep(3000);
                        System.out.println("执行完毕");
                    } catch (InterruptedException e) {
                        System.out.println("异常："+e.getMessage());
                    }
                }
            });
            System.out.println("任务"+i+"提交");
        }
        try {
            Thread.sleep(1000L);
            poolExecutor.shutdown();
            poolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("追加任务");
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void threadPoolTest8(){
        ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(2), new RejectedExecutionHandler() {//异常信息为试图及那个工作中的线程停止
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.err.println("任务被拒绝执行");
            }
        });
        for(int i=0;i<15;i++){
            int n=i;
            poolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("线程："+Thread.currentThread().getName()+"在执行任务"+n);
                        Thread.sleep(3000);
                        System.out.println("执行完毕");
                    } catch (InterruptedException e) {
                        System.out.println("异常："+e.getMessage());
                    }
                }
            });
            System.out.println("任务"+i+"提交");
        }
        try {
            Thread.sleep(1000L);
            List<Runnable>list=poolExecutor.shutdownNow();
            poolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("追加任务");
                }
            });
            System.out.println("被终止执行等待队列的任务数"+list.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
