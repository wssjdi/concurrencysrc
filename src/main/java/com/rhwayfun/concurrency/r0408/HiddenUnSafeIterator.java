package com.rhwayfun.concurrency.r0408;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wssjdi@gmail.com
 */
public class HiddenUnSafeIterator {

    private final Set<Integer> set = new HashSet<>();

    public synchronized void add(Integer i){set.add(i);}
    public synchronized void remove(Integer i){set.remove(i);}

    public void addTenThings(){
        Random random = new Random();
        for (int i = 0; i < 10; i++)
            add(random.nextInt(100));
        System.out.println("DEBUG: added ten elements to " + set);
    }

    public static void main(String[] args){
        if (args != null && args.length > 0){
            System.out.println("args:" + args[0] + "-->" + args[1] + "-->" + args[2]);
        }
        final HiddenUnSafeIterator hi = new HiddenUnSafeIterator();
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 15; i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    hi.addTenThings();
                }
            });
        }
    }
}
