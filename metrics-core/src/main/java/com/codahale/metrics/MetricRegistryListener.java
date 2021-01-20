package com.codahale.metrics;

import java.util.EventListener;

/**
 * 注册表中事件的监听器。监听器必须是线程安全的。
 */
public interface MetricRegistryListener extends EventListener {
    /**
     * MetricRegistryListener的一个无操作实现类
     */
    abstract class Base implements MetricRegistryListener {
        @Override
        public void onGaugeAdded(String name, Gauge<?> gauge) {
        }

        @Override
        public void onGaugeRemoved(String name) {
        }

        @Override
        public void onCounterAdded(String name, Counter counter) {
        }

        @Override
        public void onCounterRemoved(String name) {
        }

        @Override
        public void onHistogramAdded(String name, Histogram histogram) {
        }

        @Override
        public void onHistogramRemoved(String name) {
        }

        @Override
        public void onMeterAdded(String name, Meter meter) {
        }

        @Override
        public void onMeterRemoved(String name) {
        }

        @Override
        public void onTimerAdded(String name, Timer timer) {
        }

        @Override
        public void onTimerRemoved(String name) {
        }
    }

    /**
     * 在将Gauge添加到注册表时调用。
     *
     * @param name  the gauge's name
     * @param gauge the gauge
     */
    void onGaugeAdded(String name, Gauge<?> gauge);

    /**
     * 在将Gauge从注册表移除时调用。
     *
     * @param name the gauge's name
     */
    void onGaugeRemoved(String name);

    /**
     * 在将Counter添加到注册表时调用。
     *
     * @param name    the counter's name
     * @param counter the counter
     */
    void onCounterAdded(String name, Counter counter);

    /**
     * 在将Counter从注册表移除时调用。
     *
     * @param name the counter's name
     */
    void onCounterRemoved(String name);

    /**
     * 在将Histogram添加到注册表时调用。
     *
     * @param name      the histogram's name
     * @param histogram the histogram
     */
    void onHistogramAdded(String name, Histogram histogram);

    /**
     * 在将Histogram从注册表移除时调用。
     *
     * @param name the histogram's name
     */
    void onHistogramRemoved(String name);

    /**
     * 在将Meter添加到注册表时调用。
     *
     * @param name  the meter's name
     * @param meter the meter
     */
    void onMeterAdded(String name, Meter meter);

    /**
     * 在将Meter从注册表移除时调用。
     *
     * @param name the meter's name
     */
    void onMeterRemoved(String name);

    /**
     * 在将Timer添加到注册表时调用。
     *
     * @param name  the timer's name
     * @param timer the timer
     */
    void onTimerAdded(String name, Timer timer);

    /**
     * 在将Timer从注册表移除时调用。
     *
     * @param name the timer's name
     */
    void onTimerRemoved(String name);
}
