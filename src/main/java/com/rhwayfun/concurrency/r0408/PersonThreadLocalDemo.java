package com.rhwayfun.concurrency.r0408;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by wssjdi@gmail.com
 */
public class PersonThreadLocalDemo {

    private static final ThreadLocal<Person> personLocal = new ThreadLocal<>();
    private static final Random ran = new Random();
    private static final DateFormat format = new SimpleDateFormat("HH:mm:ss");

    /**
     * 不同的线程并发修改Person的age属性
     */
    static class Wokrer implements Runnable{
        @Override
        public void run() {
            doExec();
        }

        private void doExec() {
            System.out.println(Thread.currentThread().getName() + " start task at "
                    + format.format(new Date()));
            //不同的线程会会将age属性设置成不同的值
            int age = ran.nextInt(20);
            Person p = getPerson();
            //设置年龄
            p.setAge(age);
            System.out.println(Thread.currentThread().getName() + ": set age to " + p.getAge() + " at "
                + format.format(new Date()));
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ": get age " + p.getAge() + " at "
                + format.format(new Date()));
        }

        protected Person getPerson() {
            Person p = personLocal.get();
            if (p == null){
                p = new Person();
                personLocal.set(p);
            }
            return p;
        }
    }

    public static void main(String[] args){
        Wokrer wokrer = new Wokrer();
        new Thread(wokrer,"worker-1").start();
        new Thread(wokrer,"worker-2").start();
    }
}
