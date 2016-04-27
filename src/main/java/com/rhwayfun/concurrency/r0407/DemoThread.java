package com.rhwayfun.concurrency.r0407;

import java.util.concurrent.BlockingQueue;

/**
 * Created by rhwayfun on 16-4-7.
 */
public class DemoThread implements Runnable {

    private BlockingQueue<Runnable> workQueue;
    private volatile boolean shutdown = false;

    public DemoThread(BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
    }

    @Override
    public void run() {
        while (!shutdown){
            try {
                Runnable job = workQueue.take();
                job.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void stopToSelf(){
        shutdown = true;
        //调用interrupt方法让等待在工作队列打算出队的线程从wait方法返回
        new Thread(this).interrupt();
    }
}
