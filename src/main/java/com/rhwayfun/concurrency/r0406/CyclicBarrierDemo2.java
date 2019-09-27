package com.rhwayfun.concurrency.r0406;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by wssjdi@gmail.com
 */
public class CyclicBarrierDemo2 implements Runnable{

    /**
     * 创建4个屏障
     * 表示4个线程并发统计文件的字符数
     * this:表示4个屏障用完后执行当前线程
     */
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(4,this);

    /**
     * 日期格式器
     */
    private DateFormat format = new SimpleDateFormat("HH:mm:ss");

    /**
     * 适用线程池执行线程
     */
    private Executor executor = Executors.newFixedThreadPool(4);

    /**
     * 保存每个线程执行的结果
     */
    private Map<String,Integer> result = new ConcurrentHashMap<String, Integer>();

    /**
     * 随机数生成器
     */
    private Random random = new Random();

    /**
     * 统计方法
     */
    private void count(){
        for (int i = 0; i < 4; i++){
            executor.execute(new Runnable() {
                public void run() {
                    //计算当前文件的字符数
                    result.put(Thread.currentThread().getName(),random.nextInt(5));
                    System.out.println(Thread.currentThread().getName() + " finish task at "+ format.format(new Date()));
                    //计算完成插入屏障
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void run() {
        int res = 0;
        //汇总每个线程的执行结果
        for (Map.Entry<String,Integer> entry : result.entrySet()){
            res += entry.getValue();
        }
        //将结果保存到map中
        result.put("result",res);
        System.out.println("final result:" + res);
    }

    public static void main(String[] args){
        CyclicBarrierDemo2 c = new CyclicBarrierDemo2();
        c.count();
    }
}
