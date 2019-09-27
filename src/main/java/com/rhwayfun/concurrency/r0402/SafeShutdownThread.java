package com.rhwayfun.concurrency.r0402;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by wssjdi@gmail.com
 */
public class SafeShutdownThread {
    public static void main(String[] args) throws InterruptedException {
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        Runner one = new Runner();
        //创建第一个计数线程，该线程使用jdk自带的中断方法执行中断
        Thread threadOne = new Thread(one,"ThreadOne");
        //执行第一个线程
        threadOne.start();
        //threadOne休眠一秒，然后由main thread执行中断
        TimeUnit.SECONDS.sleep(1);
        threadOne.interrupt();
        System.out.println("ThreadOne is interrupted ? " + threadOne.isInterrupted());
        System.out.println("main thread interrupt ThreadOne at " + format.format(new Date()));

        //创建第二个线程，该线程使用cancel方法执行中断
        Runner two = new Runner();
        Thread threadTwo = new Thread(two,"ThreadTwo");
        threadTwo.start();
        //休眠一秒，然后调用cancel方法中断线程
        TimeUnit.SECONDS.sleep(1);
        two.cancel();
        System.out.println("ThreadTwo is interrupted ? " + threadTwo.isInterrupted());
        System.out.println("main thread interrupt ThreadTwo at " + format.format(new Date()));
    }

    /**
     * 该线程是一个计数线程
     */
    private static class Runner implements Runnable{
        //变量i
        private long i;
        //是否继续运行的标志
        //这里使用volatile关键字可以保证多线程并发访问该变量的时候
        //其他线程都可以感知到该变量值的变化。这样所有线程都会从共享
        //内存中取值
        private volatile boolean on = true;
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()){
                i++;
            }
            System.out.println("Count i = " + i);
        }

        //让线程终止的方法
        public void cancel(){
            on = false;
        }
    }
}