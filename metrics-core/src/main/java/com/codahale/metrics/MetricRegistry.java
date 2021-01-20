package com.codahale.metrics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 一个metrics实例的注册表。
 */
public class MetricRegistry implements MetricSet {
    /**
     * 名称以.连接
     * @param name  第一个名称
     * @param names 剩余名称
     */
    public static String name(String name, String... names) {
        final StringBuilder builder = new StringBuilder();
        append(builder, name);
        if (names != null) {
            for (String s : names) {
                append(builder, s);
            }
        }
        return builder.toString();
    }

    /**
     * @param klass    类，用类名作为第一个名称
     * @param names    剩下的名称
     */
    public static String name(Class<?> klass, String... names) {
        return name(klass.getName(), names);
    }

    /**
     * 连接函数
     */
    private static void append(StringBuilder builder, String part) {
        if (part != null && !part.isEmpty()) {
            if (builder.length() > 0) {
                builder.append('.');
            }
            builder.append(part);
        }
    }

    private final ConcurrentMap<String, Metric> metrics;
    private final List<MetricRegistryListener> listeners;

    /**
     * Creates a new {@link MetricRegistry}.
     */
    public MetricRegistry() {
        this.metrics = buildMap();
        this.listeners = new CopyOnWriteArrayList<MetricRegistryListener>();
    }

    /**
     * Creates a new {@link ConcurrentMap} implementation for use inside the registry. Override this
     * to create a {@link MetricRegistry} with space- or time-bounded metric lifecycles, for
     * example.
     *
     * @return a new {@link ConcurrentMap}
     */
    protected ConcurrentMap<String, Metric> buildMap() {
        return new ConcurrentHashMap<String, Metric>();
    }

    /**
     * 给定一个Metric，并以给定名称进行注册
     *
     * @param name   the name of the metric
     * @param metric the metric
     * @param <T>    the type of the metric
     * @return {@code metric}
     * @throws IllegalArgumentException if the name is already registered
     */
    @SuppressWarnings("unchecked")
    public <T extends Metric> T register(String name, T metric) throws IllegalArgumentException {
        if (metric instanceof MetricSet) {
            registerAll(name, (MetricSet) metric);
        } else {
            final Metric existing = metrics.putIfAbsent(name, metric);
            if (existing == null) {
                onMetricAdded(name, metric);
            } else {
                throw new IllegalArgumentException("A metric named " + name + " already exists");
            }
        }
        return metric;
    }

    /**
     * 给定一组Metric，进行注册
     *
     * @param metrics    a set of metrics
     * @throws IllegalArgumentException 当有一个重复时就会报错
     */
    public void registerAll(MetricSet metrics) throws IllegalArgumentException {
        registerAll(null, metrics);
    }

    /**
     * 创建一个新的Counter并将其注册为给定名称。
     *
     * @param name the name of the metric
     * @return a new {@link Counter}
     */
    public Counter counter(String name) {
        return getOrAdd(name, MetricBuilder.COUNTERS);
    }

    /**
     * 创建一个新的Histogram并将其注册为给定名称。
     *
     * @param name the name of the metric
     * @return a new {@link Histogram}
     */
    public Histogram histogram(String name) {
        return getOrAdd(name, MetricBuilder.HISTOGRAMS);
    }

    /**
     * 创建一个新的Meter并将其注册为给定名称。
     *
     * @param name the name of the metric
     * @return a new {@link Meter}
     */
    public Meter meter(String name) {
        return getOrAdd(name, MetricBuilder.METERS);
    }

    /**
     * 创建一个新的Timer并将其注册为给定名称。
     *
     * @param name the name of the metric
     * @return a new {@link Timer}
     */
    public Timer timer(String name) {
        return getOrAdd(name, MetricBuilder.TIMERS);
    }

    /**
     * 移除指定名称的metrics
     *
     * @param name the name of the metric
     * @return whether or not the metric was removed
     */
    public boolean remove(String name) {
        final Metric metric = metrics.remove(name);
        if (metric != null) {
            onMetricRemoved(name, metric);
            return true;
        }
        return false;
    }

    /**
     * 删除所有与给定过滤器匹配的Metrics
     *
     * @param filter a filter
     */
    public void removeMatching(MetricFilter filter) {
        for (Map.Entry<String, Metric> entry : metrics.entrySet()) {
            if (filter.matches(entry.getKey(), entry.getValue())) {
                remove(entry.getKey());
            }
        }
    }

    /**
     * 将一个MetricRegistryListener添加到监听器集合中。
     * 监听器将按照添加顺序通知。
     * 监听器在首次注册时将收到所有现有Metrics的通知。（将所有现有Metrics添加到这个新注册表中）
     *
     * @param listener the listener that will be notified
     */
    public void addListener(MetricRegistryListener listener) {
        listeners.add(listener);

        for (Map.Entry<String, Metric> entry : metrics.entrySet()) {
            notifyListenerOfAddedMetric(listener, entry.getValue(), entry.getKey());
        }
    }

    /**
     * 从注册表集合中移除一个MetricRegistryListener
     */
    public void removeListener(MetricRegistryListener listener) {
        listeners.remove(listener);
    }

    /**
     * 返货所有现有metrics的名称
     * 返回的是一个不可被操作的对象unmodifiableSortedSet
     */
    public SortedSet<String> getNames() {
        return Collections.unmodifiableSortedSet(new TreeSet<String>(metrics.keySet()));
    }

    /**
     * 返回注册表中所有满足过滤条件的Gauge及其名称
     */
    public SortedMap<String, Gauge> getGauges() {
        return getGauges(MetricFilter.ALL);
    }

    /**
     * 返回注册表中所有满足过滤条件的Gauge及其名称
     */
    public SortedMap<String, Gauge> getGauges(MetricFilter filter) {
        return getMetrics(Gauge.class, filter);
    }

    /**
     * 返回注册表中所有满足过滤条件的Counter及其名称
     */
    public SortedMap<String, Counter> getCounters() {
        return getCounters(MetricFilter.ALL);
    }

    /**
     * 返回注册表中所有满足过滤条件的Counter及其名称
     */
    public SortedMap<String, Counter> getCounters(MetricFilter filter) {
        return getMetrics(Counter.class, filter);
    }

    /**
     * 返回注册表中所有满足过滤条件的Histogram及其名称
     */
    public SortedMap<String, Histogram> getHistograms() {
        return getHistograms(MetricFilter.ALL);
    }

    /**
     * 返回注册表中所有满足过滤条件的Histogram及其名称
     */
    public SortedMap<String, Histogram> getHistograms(MetricFilter filter) {
        return getMetrics(Histogram.class, filter);
    }

    /**
     * 返回注册表中所有满足过滤条件的Meter及其名称
     */
    public SortedMap<String, Meter> getMeters() {
        return getMeters(MetricFilter.ALL);
    }

    /**
     * 返回注册表中所有满足过滤条件的Meter及其名称
     */
    public SortedMap<String, Meter> getMeters(MetricFilter filter) {
        return getMetrics(Meter.class, filter);
    }

    /**
     * 返回注册表中所有满足过滤条件的Timer及其名称
     */
    public SortedMap<String, Timer> getTimers() {
        return getTimers(MetricFilter.ALL);
    }

    /**
     * 返回注册表中所有满足过滤条件的Timer及其名称
     */
    public SortedMap<String, Timer> getTimers(MetricFilter filter) {
        return getMetrics(Timer.class, filter);
    }

    /**
     * 获取或者添加一个Metrics
     * @param name
     * @param builder
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T extends Metric> T getOrAdd(String name, MetricBuilder<T> builder) {
        final Metric metric = metrics.get(name);
        if (builder.isInstance(metric)) {
            return (T) metric;
        } else if (metric == null) {
            try {
                return register(name, builder.newMetric());
            } catch (IllegalArgumentException e) {
                final Metric added = metrics.get(name);
                if (builder.isInstance(added)) {
                    return (T) added;
                }
            }
        }
        throw new IllegalArgumentException(name + " is already used for a different type of metric");
    }

    /**
     * 指定一个metrics类型，指定一个metricsFilter
     * 返回所有现有Metrics中，符合指定类型且满足过滤条件的metrics map
     * @param klass 指定的metrics类型
     * @param filter 指定的过滤器
     * @param <T> Metrics类型
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T extends Metric> SortedMap<String, T> getMetrics(Class<T> klass, MetricFilter filter) {
        final TreeMap<String, T> timers = new TreeMap<String, T>();
        for (Map.Entry<String, Metric> entry : metrics.entrySet()) {
            if (klass.isInstance(entry.getValue()) && filter.matches(entry.getKey(),
                                                                     entry.getValue())) {
                timers.put(entry.getKey(), (T) entry.getValue());
            }
        }
        return Collections.unmodifiableSortedMap(timers);
    }

    /**
     * 新增Metrics到注册表中
     * @param name
     * @param metric
     */
    private void onMetricAdded(String name, Metric metric) {
        for (MetricRegistryListener listener : listeners) {
            notifyListenerOfAddedMetric(listener, metric, name);
        }
    }

    /**
     * 新增Metrics到注册表中，判断Metrics类型
     * @param name
     * @param metric
     */
    private void notifyListenerOfAddedMetric(MetricRegistryListener listener, Metric metric, String name) {
        if (metric instanceof Gauge) {
            listener.onGaugeAdded(name, (Gauge<?>) metric);
        } else if (metric instanceof Counter) {
            listener.onCounterAdded(name, (Counter) metric);
        } else if (metric instanceof Histogram) {
            listener.onHistogramAdded(name, (Histogram) metric);
        } else if (metric instanceof Meter) {
            listener.onMeterAdded(name, (Meter) metric);
        } else if (metric instanceof Timer) {
            listener.onTimerAdded(name, (Timer) metric);
        } else {
            throw new IllegalArgumentException("Unknown metric type: " + metric.getClass());
        }
    }

    /**
     * 从注册表中移除
     * @param name
     * @param metric
     */
    private void onMetricRemoved(String name, Metric metric) {
        for (MetricRegistryListener listener : listeners) {
            notifyListenerOfRemovedMetric(name, metric, listener);
        }
    }

    private void notifyListenerOfRemovedMetric(String name, Metric metric, MetricRegistryListener listener) {
        if (metric instanceof Gauge) {
            listener.onGaugeRemoved(name);
        } else if (metric instanceof Counter) {
            listener.onCounterRemoved(name);
        } else if (metric instanceof Histogram) {
            listener.onHistogramRemoved(name);
        } else if (metric instanceof Meter) {
            listener.onMeterRemoved(name);
        } else if (metric instanceof Timer) {
            listener.onTimerRemoved(name);
        } else {
            throw new IllegalArgumentException("Unknown metric type: " + metric.getClass());
        }
    }

    private void registerAll(String prefix, MetricSet metrics) throws IllegalArgumentException {
        for (Map.Entry<String, Metric> entry : metrics.getMetrics().entrySet()) {
            if (entry.getValue() instanceof MetricSet) {
                registerAll(name(prefix, entry.getKey()), (MetricSet) entry.getValue());
            } else {
                register(name(prefix, entry.getKey()), entry.getValue());
            }
        }
    }

    /**
     * 返回所有注册的Metrics
     * 注意这里返回的是一个不可修改的视图UnmodifiableMap
     * @return
     */
    @Override
    public Map<String, Metric> getMetrics() {
        return Collections.unmodifiableMap(metrics);
    }

    /**
     * A quick and easy way of capturing the notion of default metrics.
     * 一个快速简便的方法创建默认Metrics
     */
    private interface MetricBuilder<T extends Metric> {
        MetricBuilder<Counter> COUNTERS = new MetricBuilder<Counter>() {
            @Override
            public Counter newMetric() {
                return new Counter();
            }

            @Override
            public boolean isInstance(Metric metric) {
                return Counter.class.isInstance(metric);
            }
        };

        MetricBuilder<Histogram> HISTOGRAMS = new MetricBuilder<Histogram>() {
            @Override
            public Histogram newMetric() {
                return new Histogram(new ExponentiallyDecayingReservoir());
            }

            @Override
            public boolean isInstance(Metric metric) {
                return Histogram.class.isInstance(metric);
            }
        };

        MetricBuilder<Meter> METERS = new MetricBuilder<Meter>() {
            @Override
            public Meter newMetric() {
                return new Meter();
            }

            @Override
            public boolean isInstance(Metric metric) {
                return Meter.class.isInstance(metric);
            }
        };

        MetricBuilder<Timer> TIMERS = new MetricBuilder<Timer>() {
            @Override
            public Timer newMetric() {
                return new Timer();
            }

            @Override
            public boolean isInstance(Metric metric) {
                return Timer.class.isInstance(metric);
            }
        };

        T newMetric();

        boolean isInstance(Metric metric);
    }
}
