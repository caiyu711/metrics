package com.codahale.metrics;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * CachedGauge类单元测试
 */
public class CachedGaugeTest {
    private final AtomicInteger value = new AtomicInteger(0);
    /**
     * 构造一个CachedGauge对象，过期时间为100，单位毫秒
     * 加载函数: 每次调用+1
     */
    private final Gauge<Integer> gauge = new CachedGauge<Integer>(100, TimeUnit.MILLISECONDS) {
        @Override
        protected Integer loadValue() {
            return value.incrementAndGet();
        }
    };

    /**
     * 两次调用结果都是1。因为两次调用的时间间隔不超过100毫秒，不会调用loadValue()
     */
    @Test
    public void cachesTheValueForTheGivenPeriod() throws Exception {
        assertThat(gauge.getValue())
                .isEqualTo(1);
        assertThat(gauge.getValue())
                .isEqualTo(1);
    }

    /**
     * 第一次调用结果为1
     * 线程休眠150秒，超过了过期时间，因此缓存过期
     * 第二次、第三次调用结果为2，因为休眠后调用了一次loadValue()
     */
    @Test
    public void reloadsTheCachedValueAfterTheGivenPeriod() throws Exception {
        assertThat(gauge.getValue())
                .isEqualTo(1);

        Thread.sleep(150);

        assertThat(gauge.getValue())
                .isEqualTo(2);

        assertThat(gauge.getValue())
                .isEqualTo(2);
    }
}
