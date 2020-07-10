package com.example.com.springboot.utiles.Thread.lock;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * 自制锁
 *
 * 实现原理：
 * 当一个线程进来我判断当前标记线程变量是否为空
 * 为空显而易见 没有线程，我们将当前线程赋值给这个变量当然了为了保证线程安全我们采用AtomicReference
 * 当不为空即为当前已被锁住，我们就进入等待队列，且当前线程挂起状态不能让他再继续下去了
 */
public class YangLock{
    /**
     * AtomiicReference 起始值为空 所以我们利用compareAndSet
     * 与期望值（null）刚开始比肯定为null 所以第一次比的时候对的上，然后将当前线程也就是update值赋值给
     * atomicReference  表示当前锁时被当前线程所持有的
     */
    volatile  private AtomicReference<Thread> atomicReference=new AtomicReference<>();
    volatile  private LinkedBlockingQueue<Thread>blockingQueue=new LinkedBlockingQueue<>();//等待对列

    NetAqs nqs=new NetAqs(){
       @Override
        public boolean tryAcquire() {//尝试抢锁
            return  atomicReference.compareAndSet(null,Thread.currentThread());
        }

       @Override
        public boolean tryRelease() {
            return  atomicReference.compareAndSet(Thread.currentThread(),null);
        }
    };

    public boolean tryLock() {//我们用这个方法判断当前锁是否被其它线程占有，比对当前线程标志变量是否为空
      return nqs.tryAcquire();
    }


    public void lock() {
          nqs.acquire();
      }



    public void unlock() {//释放锁
        nqs.release();
    }

    public void lockInterruptibly() throws InterruptedException {

    }



    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }



    public Condition newCondition() {
        return null;
    }
}
