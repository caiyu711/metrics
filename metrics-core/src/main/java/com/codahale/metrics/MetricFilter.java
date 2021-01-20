package com.codahale.metrics;

/**
 * 一个用于确定metrics是否需要被报告的过滤器。
 */
public interface MetricFilter {
    /**
     * 匹配所有指标，无论类型或名称如何
     */
    MetricFilter ALL = new MetricFilter() {
        @Override
        public boolean matches(String name, Metric metric) {
            return true;
        }
    };

    /**
     * 判断metrics与过滤条件是否匹配
     *
     * @param name      the metric's name
     * @param metric    the metric
     * @return {@code true} if the metric matches the filter
     */
    boolean matches(String name, Metric metric);
}
