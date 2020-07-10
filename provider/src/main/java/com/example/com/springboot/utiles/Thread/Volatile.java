package com.example.com.springboot.utiles.Thread;

public class Volatile {
    private  static volatile  int count=1;

    public static void main(String[] args) {
        for(int j=0;j<10;j++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });
            thread.start();
            System.out.println(thread.getName()+"的线程状态"+thread.getState().toString());
        }
        System.out.println(count);
    }
}
