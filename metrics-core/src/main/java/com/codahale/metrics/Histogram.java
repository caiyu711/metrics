package com.codahale.metrics;

/**
 * 一个计算值分布的度量——直方图
 */
public class Histogram implements Metric, Sampling, Counting {
    private final Reservoir reservoir;
    private final LongAdder count;

    /**
     * Creates a new {@link Histogram} with the given reservoir.
     *
     * @param reservoir the reservoir to create a histogram from
     */
    public Histogram(Reservoir reservoir) {
        this.reservoir = reservoir;
        this.count = new LongAdder();
    }

    /**
     * Adds a recorded value.
     *
     * @param value the length of the value
     */
    public void update(int value) {
        update((long) value);
    }

    /**
     * Adds a recorded value.
     *
     * @param value the length of the value
     */
    public void update(long value) {
        count.increment();
        reservoir.update(value);
    }

    /**
     * Returns the number of values recorded.
     *
     * @return the number of values recorded
     */
    @Override
    public long getCount() {
        return count.sum();
    }

    @Override
    public Snapshot getSnapshot() {
        return reservoir.getSnapshot();
    }
}
