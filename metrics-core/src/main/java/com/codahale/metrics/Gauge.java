package com.codahale.metrics;


/**
 * 记录一个瞬时值的接口 只有一个返回当前值的方法
 */
public interface Gauge<T> extends Metric {
    /**
     * 返回当前值
     */
    T getValue();
}
