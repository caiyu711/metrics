package com.codahale.metrics;

import java.util.concurrent.TimeUnit;

/**
 * Clock类的一个实现类，可以用户手动设定时间
 */
public class ManualClock extends Clock {
    /**
     * 初始时间为0，单位：纳秒
     */
    long ticksInNanos = 0;

    /**
     * 增加指定纳秒
     * @param nanos 纳秒
     */
    public synchronized void addNanos(long nanos) {
        ticksInNanos += nanos;
    }

    /**
     * 增加指定毫秒
     * @param millis 毫秒
     */
    public synchronized void addMillis(long millis) {
        ticksInNanos += TimeUnit.MILLISECONDS.toNanos(millis);
    }

    /**
     * 增加指定小时
     * @param hours 小时
     */
    public synchronized void addHours(long hours) {
        ticksInNanos += TimeUnit.HOURS.toNanos(hours);
    }

    // TODO：可以优化成用户指定单位

    /**
     * 实现Clock.getTick()方法，返回手动设置的时间单位
     * @return
     */
    @Override
    public synchronized long getTick() {
        return ticksInNanos;
    }

    /**
     * 重写Clock.getTime()方法，返回手动设置的时间戳
     * @return
     */
    @Override
    public synchronized long getTime() {
        return TimeUnit.NANOSECONDS.toMillis(ticksInNanos);
    }
    
}
