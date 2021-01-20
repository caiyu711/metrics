package com.codahale.metrics;

import static java.lang.Math.min;

/**
 * 一个Reservoir实现，使用滑动窗口，该窗口存储了最近的n个测量值。
 */
public class SlidingWindowReservoir implements Reservoir {
    private final long[] measurements;
    private long count;

    /**
     * Creates a new {@link SlidingWindowReservoir} which stores the last {@code size} measurements.
     *
     * @param size 存储的最近size个值
     */
    public SlidingWindowReservoir(int size) {
        this.measurements = new long[size];
        this.count = 0;
    }

    @Override
    public synchronized int size() {
        return (int) min(count, measurements.length);
    }

    @Override
    public synchronized void update(long value) {
        measurements[(int) (count++ % measurements.length)] = value; // 超过后从头开始覆盖
    }

    @Override
    public Snapshot getSnapshot() {
        final long[] values = new long[size()];
        for (int i = 0; i < values.length; i++) {
            synchronized (this) {
                values[i] = measurements[i];
            }
        }
        return new Snapshot(values);
    }
}
