package com.rhwayfun.concurrency.r0407;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rhwayfun on 16-4-7.
 */
public class ExchangerDemo {

    /**
     * 交换器
     */
    private static final Exchanger<String> exchanger = new Exchanger<>();

    /**
     * 线程池
     */
    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    /**
     *
     */
    private static final DateFormat format = new SimpleDateFormat("HH:mm:ss");

    /**
     * 主线程
     * @param args
     */
    public static void main(String[] args){
        //第一个会计师进行对账
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String resA = "A's result";
                    //调用exchange方法表示当前线程已经到达了同步点
                    exchanger.exchange(resA);
                    System.out.println(Thread.currentThread().getName() + " arrives at syncPoint at "
                            + format.format(new Date()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //第二个会计师进行对账
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String resB = "B's result";
                    String resF = exchanger.exchange(resB);
                    System.out.println(Thread.currentThread().getName() + " arrives at syncPoint at "
                        + format.format(new Date()));
                    System.out.println("Is the data consistent？" + resF.equals(resB)
                        + ". A:" + resF + ", B:" + resB + " at " + format.format(new Date()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //关闭线程池
        threadPool.shutdown();
    }
}
