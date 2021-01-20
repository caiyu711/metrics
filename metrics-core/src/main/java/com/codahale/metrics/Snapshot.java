package com.codahale.metrics;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;

import static java.lang.Math.floor;

/**
 * 快照类
 */
public class Snapshot {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final long[] values;

    /**
     * Create a new {@link Snapshot} with the given values.
     *
     * @param values    an unordered set of values in the reservoir
     */
    public Snapshot(Collection<Long> values) {
        final Object[] copy = values.toArray();
        this.values = new long[copy.length];
        for (int i = 0; i < copy.length; i++) {
            this.values[i] = (Long) copy[i];
        }
        Arrays.sort(this.values); // 还进行了排序，排序才能进行计算（考虑中位数）
    }

    /**
     * Create a new {@link Snapshot} with the given values.
     *
     * @param values    an unordered set of values in the reservoir
     */
    public Snapshot(long[] values) {
        this.values = Arrays.copyOf(values, values.length);
        Arrays.sort(this.values);
    }

    /**
     * 返回给定分位数的值。分位数：[0,1]
     */
    public double getValue(double quantile) {
        if (quantile < 0.0 || quantile > 1.0) {
            throw new IllegalArgumentException(quantile + " is not in [0..1]");
        }

        if (values.length == 0) {
            return 0.0;
        }

        final double pos = quantile * (values.length + 1);

        if (pos < 1) {
            return values[0];
        }

        if (pos >= values.length) {
            return values[values.length - 1];
        }

        final double lower = values[(int) pos - 1];
        final double upper = values[(int) pos];
        return lower + (pos - floor(pos)) * (upper - lower);
    }

    /**
     * Returns the number of values in the snapshot.
     *
     * @return the number of values
     */
    public int size() {
        return values.length;
    }

    /**
     * 返回中位数
     */
    public double getMedian() {
        return getValue(0.5);
    }

    /**
     * 返回分布中第75个百分点的值。
     */
    public double get75thPercentile() {
        return getValue(0.75);
    }

    /**
     * 返回分布中第95个百分点的值。
     */
    public double get95thPercentile() {
        return getValue(0.95);
    }

    /**
     * 返回分布中第98个百分点的值。
     */
    public double get98thPercentile() {
        return getValue(0.98);
    }

    /**
     * 返回分布中第99个百分点的值。
     */
    public double get99thPercentile() {
        return getValue(0.99);
    }

    /**
     * 返回分布中第99.9个百分点的值。
     */
    public double get999thPercentile() {
        return getValue(0.999);
    }

    /**
     * Returns the entire set of values in the snapshot.
     *
     * @return the entire set of values
     */
    public long[] getValues() {
        return Arrays.copyOf(values, values.length);
    }

    /**
     * 返回最大值，因为排序所以最大值就是最后一个值
     */
    public long getMax() {
        if (values.length == 0) {
            return 0;
        }
        return values[values.length - 1];
    }

    /**
     * 返回最小值
     */
    public long getMin() {
        if (values.length == 0) {
            return 0;
        }
        return values[0];
    }

    /**
     * 平均值
     */
    public double getMean() {
        if (values.length == 0) {
            return 0;
        }

        double sum = 0;
        for (long value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    /**
     * 标准偏差
     * 标准差(Standard Deviation)描述各数据偏离平均数的距离（离均差）的平均数
     */
    public double getStdDev() {
        // two-pass algorithm for variance, avoids numeric overflow

        if (values.length <= 1) {
            return 0;
        }

        final double mean = getMean(); // 获得平均值
        double sum = 0;

        for (long value : values) {
            final double diff = value - mean; // 数据距离平均值的距离
            sum += diff * diff;
        }

        final double variance = sum / (values.length - 1);
        return Math.sqrt(variance);
    }

    /**
     * 将值写到输入的流
     */
    public void dump(OutputStream output) {
        final PrintWriter out = new PrintWriter(new OutputStreamWriter(output, UTF_8));
        try {
            for (long value : values) {
                out.printf("%d%n", value);
            }
        } finally {
            out.close();
        }
    }
}
