package com.codahale.metrics;

/**
 * 一个可以递增或递减的计数度量
 */
public class Counter implements Metric, Counting {
    private final LongAdder count;

    public Counter() {
        this.count = new LongAdder();
    }

    /**
     * 计数器 +1
     */
    public void inc() {
        inc(1);
    }

    /**
     * 计数器 +n
     */
    public void inc(long n) {
        count.add(n);
    }

    /**
     * 计数器 -1
     */
    public void dec() {
        dec(1);
    }

    /**
     * 计数器 -n
     */
    public void dec(long n) {
        count.add(-n);
    }

    /**
     * 获取当前计数器的值
     */
    @Override
    public long getCount() {
        return count.sum();
    }
}
