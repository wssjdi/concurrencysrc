package com.rhwayfun.concurrency.r0408;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wssjdi@gmail.com
 */
public class DemoThreadLocal {

    /**
     * 用来关联值与当前线程的Map
     */
    private Map<Thread,Object> localMap = Collections.synchronizedMap(new HashMap<Thread, Object>());

    /**
     * 设置值与线程关联
     * @param copyValue
     */
    public void set(Object copyValue){
        //1、key为当前访问值的线程，value为值的副本
        localMap.put(Thread.currentThread(),copyValue);
    }

    /**
     * 得到当前线程关联的值
     * @return
     */
    public Object get(){
        //获取当前线程
        Thread currentThread = Thread.currentThread();
        //根据当前线程得到值
        Object value = localMap.get(currentThread);
        if (value == null || !localMap.containsKey(currentThread)){
            value = initialValue();
            localMap.put(currentThread,value);
        }
        return value;
    }

    /**
     * 对值进行初始化
     * @return
     */
    protected Object initialValue() {
        return null;
    }
}
