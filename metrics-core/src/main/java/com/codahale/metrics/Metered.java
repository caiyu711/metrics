package com.codahale.metrics;

/**
 * 一个计算均值和指数加权速率的度量对象
 */
public interface Metered extends Metric, Counting {
    /**
     * 返回已标记的事件数量
     */
    long getCount();

    /**
     * 返回近15分钟的指数加权速率
     * 该速率与Unix命令中15分钟的平均负载具有相同的指数衰减因子
     */
    double getFifteenMinuteRate();

    /**
     * 返回近5分钟的指数加权速率
     * 该速率与Unix命令中5分钟的平均负载具有相同的指数衰减因子
     */
    double getFiveMinuteRate();

    /**
     * 返回平均指数加权速率
     */
    double getMeanRate();

    /**
     * 返回近1分钟的指数加权速率
     * 该速率与Unix命令中1分钟的平均负载具有相同的指数衰减因子
     */
    double getOneMinuteRate();
}
