package com.rhwayfun.concurrency.r0407;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by wssjdi@gmail.com
 */
public class DemoThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    //阻塞队列
    private BlockingQueue<Runnable> workQueue = null;
    //保存线程池的工作线程
    private List<DemoThread> demoThreadList = Collections.synchronizedList(new ArrayList<DemoThread>());
    //线程池状态
    private boolean isShutdown = false;
    //线程池默认的大小
    private static final int DEFAULT_WORKER_NUM = 5;
    //线程池最大的大小
    private static final int MAX_WORKER_NUM = 10;
    //线程池最小的大小
    private static final int MIN_WORKER_NUM = 1;
    //工作者线程的数量
    private int workNum;
    //线程编号
    private AtomicLong threadNum = new AtomicLong();

    public DemoThreadPool(int num) {
        workNum = num > MAX_WORKER_NUM ? MAX_WORKER_NUM : num < MIN_WORKER_NUM ? MIN_WORKER_NUM : num;
        init(workNum);
    }

    public DemoThreadPool() {
        init(DEFAULT_WORKER_NUM);
    }

    /**
     * 线程池初始化
     *
     * @param workNum
     */
    private void init(int workNum) {
        //初始化工作队列
        workQueue = new ArrayBlockingQueue<>(DEFAULT_WORKER_NUM);
        //将指定数量的工作线程加入到列表中
        for (int i = 0; i < workNum; i++) {
            demoThreadList.add(new DemoThread(workQueue));
        }
        //启动指定数量的工作线程
        for (DemoThread thread : demoThreadList) {
            Thread worker = new Thread(thread, "ThreadPool-Worker-" + threadNum.incrementAndGet());
            System.out.println("ThreadPool-Worker-" + threadNum.get() + " add to workQueue!");
            worker.start();
        }
    }

    /**
     * 执行一个任务
     *
     * @param job
     */
    public void execute(Runnable job) {
        if (isShutdown) throw new IllegalStateException("ThreadPool is shutdown!");
        if (demoThreadList != null) {
            try {
                //添加一个任务到工作队列中
                workQueue.put(job);
                System.out.println("ThreadPool receives a task!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        isShutdown = true;
        for (DemoThread t : demoThreadList) {
            t.stopToSelf();
        }
    }
}
