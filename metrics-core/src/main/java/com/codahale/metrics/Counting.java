package com.codahale.metrics;

/**
 * 一个度量类型接口，拥有计数器
 */
public interface Counting {
    /**
     * 返回当前计数
     */
    long getCount();
}
