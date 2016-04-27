package com.rhwayfun.concurrency.r0411;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by rhwayfun on 16-4-11.
 */
public class LoggerService {
    // 存放日志消息的阻塞队列
    private final BlockingQueue<String> logQueue;
    // 打印日志的消费者线程
    private final LoggerThread loggerThread;
    // 打印日志的打印器
    private PrintWriter writer;
    // 日志服务是否关闭的标志
    private boolean isShutdown;
    // 执行log方法的调用者的计数器
    private int reservations;

    public LoggerService(PrintWriter writer) {
        this.logQueue = new LinkedBlockingQueue<>(5);
        this.loggerThread = new LoggerThread(writer);
    }

    /**
     * 启动日志服务
     */
    public void start() {
        loggerThread.start();
    }

    /**
     * 记录日志
     *
     * @param msg
     * @throws InterruptedException
     */
    public void recordLog(String msg) throws InterruptedException {
        // 有条件保持对日志的添加
        // 并且在接收到关闭请求时停止往队列中填入日志
        synchronized (this) {
            if (isShutdown) throw new IllegalStateException("LoggerService is shutdown!");
            ++reservations;
        }
        // 由生产者将消息放入队列
        // 这里不放入synchronized块是因为put方法有阻塞的作用
        logQueue.put(msg);
    }

    /**
     * 停止日志服务
     */
    public void stop() {
        // 以原子方式检查关闭请求
        synchronized (this) {
            isShutdown = true;
        }
        // 让消费者线程停止从队列取日志
        loggerThread.interrupt();
    }

    /**
     * 消费者线程
     */
    private class LoggerThread extends Thread {
        private PrintWriter writer;

        public LoggerThread(PrintWriter writer) {
            this.writer = writer;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        // 持有的锁与之前的相同
                        // 如果接收到应用程序的关闭请求并且没有生产者线程继续往队列填入日志
                        // 那么就结束循环，消费者线程终止
                        synchronized (LoggerService.this) {
                            if (isShutdown && reservations == 0) break;
                        }
                        // 从队列获取生产者的日志
                        String msg = logQueue.take();
                        // 每输出一条日志就减少一个线程
                        synchronized (LoggerService.this) {
                            --reservations;
                        }
                        writer.println("Read: " + msg);
                    } catch (InterruptedException e) {
                        //恢复中断状态
                        Thread.currentThread().interrupt();
                    }
                }
            } finally {
              writer.close();
            }
        }
    }

    /**
     * 生产者线程
     */
    private static class LoggerWriter implements Runnable {
        private LoggerService service;
        private final DateFormat format = new SimpleDateFormat("HH:mm:ss");

        public LoggerWriter(LoggerService service) {
            this.service = service;
        }

        @Override
        public void run() {
            try {
                String msg = "time is " + format.format(new Date());
                System.out.println("Write: " + msg);
                service.recordLog(msg);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        LoggerService service = new LoggerService(new PrintWriter(System.out));
        //创建多个生产者线程负责创建日志
        for (int i = 0; i < 5; i++) {
            new Thread(new LoggerWriter(service)).start();
            TimeUnit.SECONDS.sleep(1);
        }
        //启动日志服务
        service.start();
        //休眠10秒
        TimeUnit.SECONDS.sleep(10);
        //关闭日志服务
        service.stop();
    }
}
