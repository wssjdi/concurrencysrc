package com.rhwayfun.concurrency.r0401;

import java.util.concurrent.*;

/**
 * Created by wssjdi@gmail.com
 */
public class ProducerConsumerModeWithBlockQueueTest {

    static class Info{
        //内容
        private String content;

        public Info(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return this.getContent();
        }
    }

    static class Producer implements Runnable{

        private final BlockingQueue<Info> blockingQueue;

        public Producer(BlockingQueue<Info> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        public void run() {
            boolean flag = true;
            for (int i = 0; i < 5; i++){
                if (flag){
                    try {
                        blockingQueue.put(new Info("contentA"));
                        System.out.println("[生产者]：contentA");
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    flag = false;
                }else {
                    try {
                        blockingQueue.put(new Info("contentB"));
                        System.out.println("[生产者]：contentB");
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    flag = true;
                }
            }
        }
    }

    static class Consumer implements Runnable{

        private final BlockingQueue<Info> blockingQueue;

        public Consumer(BlockingQueue<Info> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        public void run() {
            while (true){
                try {
                    System.out.println("[消费者]：" + blockingQueue.take());
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){
        BlockingQueue<Info> blockingQueue = new LinkedBlockingQueue<Info>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        new Thread(new Producer(blockingQueue)).start();
        new Thread(new Consumer(blockingQueue)).start();
    }
}
