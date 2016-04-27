package com.rhwayfun.concurrency.r0407;

/**
 * Created by rhwayfun on 16-4-7.
 */
public interface ThreadPool<Job extends Runnable> {
    void execute(Job job);
    void shutdown();
}
