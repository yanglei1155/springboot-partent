package com.example.com.springboot.utiles.Thread.lock;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 实际场景：比如我不要异步执行sql 我要批量执行
 * 相比于异步我们次数减少了 节省资源了
 */
public class CycleBarrierTest {
    public static void main(String[] args) throws  Exception{
        LinkedBlockingQueue<String> queue=new LinkedBlockingQueue();
        //数字时临界值触发
        CyclicBarrier cyclicBarrier=new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                System.out.println("已经有4条sql了我们开始插入数据库吧");
                for (String sql:queue){
                    System.out.println(sql);
                }
            }
        });
        for (int i=0;i<10;i++){
            new Thread(()->{
                try {
                    queue.add("data-"+Thread.currentThread());//缓存sql
                    Thread.sleep(1000L);
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread()+"插入完毕");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
         Thread.sleep(3000L);
    }


}
