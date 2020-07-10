package com.example.com.springboot.utiles.Thread;

public class StopThreadTest {
    public static void main(String[] args) throws InterruptedException{
        StopThread stopThread=new StopThread();
        stopThread.start();
        Thread.sleep(1000L);
         //stopThread.stop();//强行中断线程
         stopThread.interrupt();//优雅的终止，当线程在睡眠或者阻塞状态时 中断会生效单线程会执行完，此时会抛出线程中断异常
        while (stopThread.isAlive()){//线程未终止进入循环直至终止

        }
        stopThread.println();//线程终止打印
    }
}
