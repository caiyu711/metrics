package com.codahale.metrics;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 一个Gauge的实现类，将值缓存一段时间
 */
public abstract class CachedGauge<T> implements Gauge<T> {
    private final Clock clock;
    private final AtomicLong reloadAt;
    private final long timeoutNS;

    private volatile T value;

    /**
     * 构造函数，参数：超时时间、超时时间单位
     */
    protected CachedGauge(long timeout, TimeUnit timeoutUnit) {
        this(Clock.defaultClock(), timeout, timeoutUnit);
    }

    /**
     * 构造函数
     */
    protected CachedGauge(Clock clock, long timeout, TimeUnit timeoutUnit) {
        this.clock = clock;
        this.reloadAt = new AtomicLong(0);
        this.timeoutNS = timeoutUnit.toNanos(timeout);
    }

    /**
     * 加载并返回一个值，创建CachedGauge对象时需实现该方法
     * 当缓存过期时调用
     */
    protected abstract T loadValue();

    /**
     * 实现Gauge.getValue()方法
     * 判断当前值是否超过了设定的缓存时间(timeout)，如果超过了就重新加载一个值loadValue()并存储到value中
     * 否则就返回当前值value
     */
    @Override
    public T getValue() {
        if (shouldLoad()) {
            this.value = loadValue();
        }
        return value;
    }

    /**
     * 判断是否需要加载值，即缓存是否过期
     */
    private boolean shouldLoad() {
        for (; ; ) {
            final long time = clock.getTick(); //当前时间单位，纳秒级
            final long current = reloadAt.get(); //下次过期时间
            if (current > time) { // 如果过期时间>当前时间，说明没有过期，返回false
                return false;
            }
            if (reloadAt.compareAndSet(current, time + timeoutNS)) { // 如果过期时间<当前时间，就将下次过期时间置为 当前时间+过期时间，返回true
                return true;
            }
        }
    }
}
