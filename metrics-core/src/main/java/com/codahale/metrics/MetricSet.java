package com.codahale.metrics;

import java.util.Map;

/**
 * 指标集
 */
public interface MetricSet extends Metric {
    /**
     * A map of metric names to metrics.
     *
     * @return the metrics
     */
    Map<String, Metric> getMetrics();
}
