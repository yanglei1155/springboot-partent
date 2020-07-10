package com.example.com.springboot.utiles.Thread.lock;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**重点：aqs只是一个抽象的队列同步器，不仅仅只是作为lock
 * acquire  acquireShared 定义了抢占资源逻辑
 * tryAcquire  tryAcquireShared 定义了试图抢占资源逻辑 由使用者自己实现
 * release releaseShare 定义了 释放资源逻辑
 * tryRelease tryReleaseShare 定义了释放资源逻辑 由实现者实现
 */
public class NetAqs {
    volatile  private AtomicReference<Thread> atomicReference=new AtomicReference<>();
    volatile  private LinkedBlockingQueue<Thread> blockingQueue=new LinkedBlockingQueue<>();//等待对列
    volatile  private AtomicInteger state=new AtomicInteger(0);//信号量数量

    /**
     * 独享锁
     */
    public void acquire() {//独享锁，抢占资源
        boolean flag=true;
        while(!tryAcquire()){//当前锁已被其它线程占有，当前线程必须进入等待队列，用while防止线程被伪唤醒
            if(flag){//避免伪唤醒重复将线程加入队列
                blockingQueue.offer(Thread.currentThread());
            }else {
                LockSupport.park();//进入等待队列就挂起
                flag=false;
            }

        }
        //当锁没被其它线程占有，就将线程移除队列
        blockingQueue.remove(Thread.currentThread());
    }

    public boolean tryAcquire(){//由实现者自己定义
        throw  new  UnsupportedOperationException();
    }

    public void release(){
       if(tryRelease()){
           Iterator<Thread> iterator= blockingQueue.iterator();
           while(iterator.hasNext()){
               Thread thread= iterator.next();
               LockSupport.unpark(thread);
           }
       }
    }
    public boolean tryRelease(){
        throw  new UnsupportedOperationException();
    }

    /**
     * 共享 限流
     */

    public void acquireShare(){
        boolean flag=true;
        while(tryAcquireShare()<0){//当前锁已被其它线程占有，当前线程必须进入等待队列，用while防止线程被伪唤醒
            if(flag){//避免伪唤醒重复将线程加入队列
                blockingQueue.offer(Thread.currentThread());
            }else {
                LockSupport.park();//进入等待队列就挂起
                flag=false;
            }

        }
        //当锁没被其它线程占有，就将线程移除队列
        blockingQueue.remove(Thread.currentThread());
    }
    public int  tryAcquireShare(){
        throw  new UnsupportedOperationException();
    }

    public void releaseAcquireShare(){
       if(tryReleaseAcquireShare()){//还有多余的数量就放线程出来
           Iterator<Thread> iterator= blockingQueue.iterator();
           while(iterator.hasNext()){
               Thread thread= iterator.next();
               LockSupport.unpark(thread);
           }
       }
    }

    public boolean  tryReleaseAcquireShare(){
        throw new UnsupportedOperationException();
    }

    public AtomicInteger getState() {
        return state;
    }

    public void setState(AtomicInteger state) {
        this.state = state;
    }
}
