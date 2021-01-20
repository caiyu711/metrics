package com.codahale.metrics;

/**
 * 采样值对象
 */
public interface Sampling {
    /**
     * 返回值得快照
     */
    Snapshot getSnapshot();
}
