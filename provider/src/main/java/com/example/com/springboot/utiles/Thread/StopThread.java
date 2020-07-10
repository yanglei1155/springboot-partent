package com.example.com.springboot.utiles.Thread;

public class StopThread extends  Thread {
    int i=0;
    int j=0;
    @Override
    public void run() {
         synchronized (this){
             i++;
             try {
                 Thread.sleep(10000);//睡眠10秒
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             j++;
         }

    }

    public void println(){
        System.out.println("i="+i+"**"+"j="+j);
    }
}
