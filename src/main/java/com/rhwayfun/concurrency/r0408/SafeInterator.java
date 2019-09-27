package com.rhwayfun.concurrency.r0408;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wssjdi@gmail.com
 */
public class SafeInterator {

    private final Set<Integer> set = Collections.synchronizedSet(new HashSet<Integer>());

    public void add(Integer i){set.add(i);}
    public void remove(Integer i){set.remove(i);}

    public void addTenThings(){
        Random random = new Random();
        for (int i = 0; i < 10; i++)
            add(random.nextInt(100));
        System.out.println("DEBUG: added ten elements to " + set);
    }

    public static void main(String[] args){
        final SafeInterator hi = new SafeInterator();
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 25; i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    hi.addTenThings();
                }
            });
        }
    }
}
