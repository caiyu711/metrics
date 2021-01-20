package com.codahale.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * comment: 一个记录时间的抽象类
 */
public abstract class Clock {
    /**
     * 获取当前时间单位
     * 个人理解:
     * 区别于getTime()，getTime()可以获取到一个确切的、可以转成时间类型的值（毫秒级时间戳）,
     * getTick()获取到的是一个标识时间的值，但这个值不能代表一个确切的时间（纳秒级），可以用来进行 代码执行时间 等计算
     */
    public abstract long getTick();

    /**
     * 返回当前毫秒级时间戳（系统时间）
     */
    public long getTime() {
        return System.currentTimeMillis();
    }

    private static final Clock DEFAULT = new UserTimeClock();

    /**
     * 默认是UserTimeClock
     */
    public static Clock defaultClock() {
        return DEFAULT;
    }

    /**
     * Clock类的一个实现类，用于返回正在运行的Jvm的高分辨率时间源的当前值，纳秒为单位
     *
     * System.nanoTime() 该方法返回不能作为一个确切的时间，仅仅表示从某个固定但任意的原点时间（可能是过去、现在、未来）开始的纳秒数。
     * 对于一个jvm实例来说这个原点时间是一致的，对于不同的jvm实例则有可能不同。
     * 仅仅只有当同一个jvm实例中获取到的两个值的差值是有意义的，可以用来：
     *  1、计算某段代码的执行时间
     *  2、比较两个nanoTime值
     * 与System.currentTimeMillis()区别：
     *  1、单位不一致
     *  2、表达含义不一致。System.currentTimeMillis()可以表示一个确切的时间
     *  3、System.currentTimeMillis()由于是系统时间，所以如果调整系统时间，这个值也会发生改变。System.nanoTime()则不是，单独使用的时候可以理解成一个随机值。
     * 关于两个时间的区别可以看：
     * {@see "https://www.cnblogs.com/andy-songwei/p/10784049.html"}
     */
    public static class UserTimeClock extends Clock {
        @Override
        public long getTick() {
            return System.nanoTime();
        }
    }

    /**
     * Clock类的一个实现类，返回当前线程的CPU时间
     */
    public static class CpuTimeClock extends Clock {
        private static final ThreadMXBean THREAD_MX_BEAN = ManagementFactory.getThreadMXBean();

        @Override
        public long getTick() {
            return THREAD_MX_BEAN.getCurrentThreadCpuTime();
        }
    }
}
