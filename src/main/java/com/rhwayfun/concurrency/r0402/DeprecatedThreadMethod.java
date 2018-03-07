/* 
 * Copyright: 2018 www.chebada.com Inc. All rights reserved. 
 * 注意：本内容仅限于车巴达公司内部传阅，禁止外泄以及用于其他的商业目的 
 *
 * @Project: concurrencysrc 
 * @File: DeprecatedThreadMethod.java 
 * @Package: com.rhwayfun.concurrency.r0402
 * @Date: 2018年3月7日
 * @Author:syj11892@ly.com 
 *
 */
package com.rhwayfun.concurrency.r0402;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/** 
 * @Description 
 * @author dqc@chebada.com
 * @date 2018年3月7日 下午4:58:41 
 * @version V1.0.0
 */

public class DeprecatedThreadMethod {

  public static void main(String[] args) throws InterruptedException {
      DateFormat format = new SimpleDateFormat("HH:mm:ss");
      Thread printThread = new Thread(new Runner(),"PrintThread");
      //设为守护线程
      printThread.setDaemon(true);
      //开始执行
      printThread.start();
      //休眠3秒，也就是PrintThread运行了3秒
      TimeUnit.SECONDS.sleep(3);
      //尝试暂停
      printThread.suspend();
      System.out.println("main thread suspend PrintThread at " + format.format(new Date()));
      TimeUnit.SECONDS.sleep(3);
      //将PrintThread进行恢复，继续输出内容
      printThread.resume();
      System.out.println("main thread resume PrintThread at " + format.format(new Date()));
      TimeUnit.SECONDS.sleep(3);
      //尝试终止PrintThread，停止输出内容
      printThread.stop();
      System.out.println("main thread stop PrintThread at " + format.format(new Date()));
      TimeUnit.SECONDS.sleep(3);
  }

  /**
   * 该任务实现每隔一秒打印信息
   */
  static class Runner implements Runnable{
      public void run() {
          DateFormat format = new SimpleDateFormat("HH:mm:ss");
          while (true){
              System.out.println(Thread.currentThread().getName() + " run at " + format.format(new Date()));
              //休眠一秒后继续打印
              try {
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
          }
      }
  }

}
