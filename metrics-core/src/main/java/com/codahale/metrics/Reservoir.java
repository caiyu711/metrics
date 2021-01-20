package com.codahale.metrics;

/**
 * 一个统计数据的库
 */
public interface Reservoir {
    /**
     * 返回记录的值的数量
     */
    int size();

    /**
     * 向库中增加一个新的记录值
     */
    void update(long value);

    /**
     * 返回一个存储库中值的快照
     */
    Snapshot getSnapshot();
}
