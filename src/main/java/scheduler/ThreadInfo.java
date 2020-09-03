package scheduler;

import java.util.concurrent.Semaphore;

public class ThreadInfo {

    Thread thread;

    //控制同时访问特定资源的线程数量

    private Semaphore semaphore;

    public Semaphore getPausingSemaphore() {
        return semaphore;
    }

    public ThreadInfo(Thread thread){
        this.thread = thread;
        //信号量初始化为0
        semaphore =  new Semaphore(0);

    }

    @Override
    public String toString() {
        return "线程："+thread.getName();

    }
}
