package com.example.com.springboot.utiles.Thread;

import net.bytebuddy.asm.Advice;

public class SynchronizedTest implements Runnable {
     static   int i=0;
       static synchronized void add (){
         try {
             Thread.sleep(2000L);
             i++;
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
    }
    @Override
    public void run() {

            add();

        System.out.println(i);
    }

    public static void main(String[] args) throws  Exception{
        Thread thread1=new Thread(new SynchronizedTest());
        Thread thread2=new Thread(new SynchronizedTest());
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}
