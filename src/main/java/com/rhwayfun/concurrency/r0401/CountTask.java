package com.rhwayfun.concurrency.r0401;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Created by rhwayfun on 16-3-19.
 */
public class CountTask extends RecursiveTask<Integer> {
    //阈值
    private static final int THRESHHOLD = 2;
    //开始
    private int start;
    //结束
    private int end;

    public CountTask(int start,int end){
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        //判断能够结算
        boolean canCompute = (end - start) <= THRESHHOLD;
        if (!canCompute) {

        } else {
            for (int i = start; i <= end; i++){
                sum += i;
            }
        }
        return sum;
    }

    public static void main(String[] args){
        ForkJoinPool pool = new ForkJoinPool();
        //生成一个计算任务
        CountTask task = new CountTask(1,4);
        //执行任务
        Future<Integer> result = pool.submit(task);
        try {
            System.out.println("返回结果：" + result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
