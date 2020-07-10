package com.example.com.springboot.utiles.Thread.lock;

public class NetSemaphor {
    NetAqs netAqs=new NetAqs(){
        @Override
        public boolean tryReleaseAcquireShare() {//释放就增加 state+1
           return  getState().incrementAndGet()>0;
        }

        @Override
        public int tryAcquireShare() {
          while (true){
                Integer n=getState().get();
                int count=n-1;
                if(count<=0||n<0){
                    return  -1;
                }
                if(getState().compareAndSet(n,count)){
                    return  1;
                }
             }
            }

    };
    public NetSemaphor(int count){
        netAqs.getState().set(count);
    }

    public void acquire(){
        netAqs.acquireShare();
    }

    public  void release(){
        netAqs.releaseAcquireShare();
    }
}
