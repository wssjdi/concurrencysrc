package com.rhwayfun.concurrency.r0407;

/**
 * Created by wssjdi@gmail.com
 */
public interface ThreadPool<Job extends Runnable> {
    void execute(Job job);
    void shutdown();
}
