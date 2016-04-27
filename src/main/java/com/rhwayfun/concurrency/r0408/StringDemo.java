package com.rhwayfun.concurrency.r0408;

/**
 * Created by rhwayfun on 16-4-8.
 */
public class StringDemo {

    public static void main(String[] args){
        String s1 = "We are happy";
        System.out.println(s1.replaceAll("\\s","%20"));
    }
}
