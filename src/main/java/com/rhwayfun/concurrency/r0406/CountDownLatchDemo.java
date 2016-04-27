package com.rhwayfun.concurrency.r0406;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by rhwayfun on 16-4-6.
 */
public class CountDownLatchDemo {

    //参数2表示一个计数器
    //这里可以理解为等待多少个线程执行完毕
    static CountDownLatch countDownLatch = new CountDownLatch(2);
    static final DateFormat format = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) throws InterruptedException {

        //第一个读取的线程
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                long start = System.currentTimeMillis();
                for (;;){
                    if (System.currentTimeMillis() - start > 1000 * 10){
                        break;
                    }
                }
                System.out.println(Thread.currentThread().getName() + " finished task at " + format.format(new Date()));
                countDownLatch.countDown();
            }
        });

        //第二个线程开始读取
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                long start = System.currentTimeMillis();
                for (;;){
                    if (System.currentTimeMillis() - start > 1000 * 5){
                        break;
                    }
                }
                System.out.println(Thread.currentThread().getName() + " finished task at " + format.format(new Date()));
                countDownLatch.countDown();
            }
        }, "Thread-2");

        System.out.println(Thread.currentThread().getName() + " start task at " + format.format(new Date()));

        thread1.start();
        thread2.start();

        countDownLatch.await();

        System.out.println(Thread.currentThread().getName() + " ended task at " + format.format(new Date()));
    }
}
