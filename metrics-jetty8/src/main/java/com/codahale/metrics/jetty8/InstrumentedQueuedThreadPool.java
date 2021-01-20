package com.codahale.metrics.jetty8;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * 排队线程池
 */
public class InstrumentedQueuedThreadPool extends QueuedThreadPool {
    public InstrumentedQueuedThreadPool(MetricRegistry registry) {
        super();
        /**
         * 空闲线程占比
         */
        registry.register(name(QueuedThreadPool.class, "percent-idle"), new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(getIdleThreads(),
                                getThreads());
            }
        });
        /**
         * 活跃线程数
         */
        registry.register(name(QueuedThreadPool.class, "active-threads"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return getThreads();
            }
        });
        /**
         * 空闲线程数
         */
        registry.register(name(QueuedThreadPool.class, "idle-threads"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return getIdleThreads();
            }
        });
        registry.register(name(QueuedThreadPool.class, "jobs"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                // This assumes the QueuedThreadPool is using a BlockingArrayQueue or
                // ArrayBlockingQueue for its queue, and is therefore a constant-time operation.
                return getQueue().size();
            }
        });
    }
}
