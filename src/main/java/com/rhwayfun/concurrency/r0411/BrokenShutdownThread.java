package com.rhwayfun.concurrency.r0411;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by rhwayfun on 16-4-11.
 */
public class BrokenShutdownThread extends Thread {

    //是否继续运行的标志
    private static volatile boolean on = true;
    //阻塞队列
    private final BlockingQueue<BigInteger> queue;

    public BrokenShutdownThread(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            /*while (on) {
                //生产者一次可以放40个数
                for (int i = 0; i < 40; i++){
                    queue.put(p = p.nextProbablePrime());
                    System.out.println(Thread.currentThread().getName() + ": put value " + p);
                }
            }*/
            while (on && !Thread.currentThread().isInterrupted()) {
                //生产者一次可以放40个数
                for (int i = 0; i < 40; i++){
                    queue.put(p = p.nextProbablePrime());
                    System.out.println(Thread.currentThread().getName() + ": put value " + p);
                }
            }
        } catch (InterruptedException e) {
            //让线程退出
            return;
        }
    }

    public void cancel() {
        on = false;
        interrupt();
    }

    /**
     * 消费者线程
     */
    static class Consumer extends Thread{
        //阻塞队列
        private final BlockingQueue<BigInteger> queue;

        public Consumer(BlockingQueue<BigInteger> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                /*while (on) {
                    //消费者一次只能消费1个数
                    System.out.println(Thread.currentThread().getName() + ": get value " + queue.take());
                }*/
                while (on && !Thread.currentThread().isInterrupted()) {
                    //消费者一次只能消费1个数
                    System.out.println(Thread.currentThread().getName() + ": get value " + queue.take());
                }
                System.out.println("work done!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<BigInteger> queue = new LinkedBlockingQueue<>(5);
        BrokenShutdownThread producer = new BrokenShutdownThread(queue);
        //启动计数线程
        producer.start();
        TimeUnit.SECONDS.sleep(1);
        new Consumer(queue).start();
        TimeUnit.SECONDS.sleep(1);
        producer.cancel();
    }
}
