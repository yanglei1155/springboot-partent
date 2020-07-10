package com.example.com.springboot.utiles.Thread;

public class ThreadTest {
    private static Thread thread1;
    public static void main(String[] args) throws  Exception {
        System.out.println("*******"+"new->Runable->终止"+"**********");
        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("当前执行状态"+Thread.currentThread().getState().toString());
                System.out.println("执行了");
            }
        }) ;
     System.out.println("运行之前状态"+thread1.getState().toString());
     thread1.start();
     Thread.sleep(2000L);
     System.out.println("睡眠两秒后状态"+thread1.getState().toString());
     System.out.println("*******"+"new->Runable->睡眠->Runable->终止"+"**********");
     Thread thread2 =new Thread(new Runnable() {
         @Override
         public void run() {
             try {
                 Thread.sleep(1500);
             }catch ( InterruptedException e){
                 e.printStackTrace();
             }
             System.out.println("当前执行状态"+Thread.currentThread().getState().toString());
             System.out.println("执行了");
         }
     });
     System.out.println("运行前状态"+thread2.getState().toString());
     thread2.start();
     System.out.println("start后当前状态"+thread2.getState().toString());
     Thread.sleep(200L);
     System.out.println("睡眠两毫秒状态"+thread2.getState().toString());
     Thread.sleep(3000L);
     System.out.println("睡眠3秒状态"+thread2.getState().toString());
     System.out.println();
     System.out.println("*******"+"new->运行->阻塞->Runable->终止"+"**********");
     Thread thread3=new Thread(new Runnable() {
         @Override
         public void run() {
           synchronized (ThreadTest.class){
               System.out.println("当前执行状态"+Thread.currentThread().getState().toString());
               System.out.println("执行了");
           }
         }
     });
     synchronized (ThreadTest.class){
         System.out.println("运行前状态"+thread3.getState().toString());
         thread3.start();
         System.out.println("当前状态"+thread3.getState().toString());
         Thread.sleep(200L);
         System.out.println("睡眠2毫秒后状态"+thread3.getState().toString());
     }
     System.out.println("刚释放锁后状态"+thread3.getState().toString());
     Thread.sleep(3000L);//等待代码块锁释放被线程抢到
     System.out.println("释放锁喉u等待3秒状态"+thread3.getState().toString());
  }
}
