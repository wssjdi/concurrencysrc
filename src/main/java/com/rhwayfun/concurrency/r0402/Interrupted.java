/* 
 * Copyright: 2018 www.chebada.com Inc. All rights reserved. 
 * 注意：本内容仅限于车巴达公司内部传阅，禁止外泄以及用于其他的商业目的 
 *
 * @Project: concurrencysrc 
 * @File: Interrupted.java 
 * @Package: com.rhwayfun.concurrency.r0402
 * @Date: 2018年3月7日
 * @Author:syj11892@ly.com 
 *
 */
package com.rhwayfun.concurrency.r0402;

import java.util.concurrent.TimeUnit;

/** 
 * @Description 
 * @author dqc@chebada.com
 * @date 2018年3月7日 下午5:05:54 
 * @version V1.0.0
 */

public class Interrupted {


  public static void main(String[] args){
      //创建一个休眠线程
      Thread sleepThread = new Thread(new SleepThread(),"SleepThread");
      //设为守护线程
      sleepThread.setDaemon(true);
      //创建一个忙线程
      Thread busyThread = new Thread(new BusyThread(),"BusyThread");
      //把该线程设为守护线程
      //守护线程只有当其他前台线程全部退出之后才会结束
      busyThread.setDaemon(true);
      //启动休眠线程
      sleepThread.start();
      //启动忙线程
      busyThread.start();
      //休眠5秒，让两个线程充分运行
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      //尝试中断线程
      //只需要调用interrupt方法
      sleepThread.interrupt();
      busyThread.interrupt();
      //查看这两个线程是否被中断了
      System.out.println("SleepThread interrupted is " + sleepThread.isInterrupted());
      System.out.println("BusyThread interrupted is " + busyThread.isInterrupted());
      //防止sleepThread和busyThread立刻退出
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }

  /**
   * 不断休眠
   */
  static class SleepThread implements Runnable{
      public void run() {
          while (true){
              try {
                  TimeUnit.SECONDS.sleep(10);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      }
  }

  /**
   * 不断等待
   */
  static class BusyThread implements Runnable{
      public void run() {
          while (true){
              //忙等待
          }
      }
  }  
  
}
