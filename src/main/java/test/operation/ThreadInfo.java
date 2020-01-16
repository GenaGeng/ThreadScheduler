package test.operation;

import java.util.concurrent.Semaphore;

public class ThreadInfo {

    Thread thread;
    private Semaphore semaphore;

    public Semaphore getPausingSemaphore() {
        return semaphore;
    }

    public ThreadInfo(Thread thread){
        this.thread = thread;
        semaphore =  new Semaphore(0);

    }

    @Override
    public String toString() {
        return "线程："+thread.getName();

    }
}
