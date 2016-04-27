package com.rhwayfun.concurrency.r0406;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by rhwayfun on 16-4-6.
 */
public class SemaphoreDemo {

    /**
     * 并发执行的线程数
     */
    private static final int THREAD_COUNT = 10;

    /**
     * 线程池
     */
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

    /**
     * 信号量
     * 只有10个许可证
     */
    private static Semaphore semaphore = new Semaphore(2);

    public static void main(String[] args){
        for (int i = 0; i < THREAD_COUNT; i++){
            final int num = i;
            threadPool.execute(new Runnable() {
                public void run() {
                    try {
                        //获取许可证
                        semaphore.acquire();
                        System.out.println(Thread.currentThread().getName() + " acquire permit " + num);
                        //输出信息
                        for (int i = 0; i < 999999;i++);
                        System.out.println("save data!");
                        //释放许可证
                        semaphore.release();
                        System.out.println(Thread.currentThread().getName() + " release permit " + num);
                        //当前可用的许可证
                        System.out.println("     current available permits:" + semaphore.availablePermits());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        threadPool.shutdown();
    }
}
