package com.codahale.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * 一个长整型数值的随机采样库
 */
public class UniformReservoir implements Reservoir {
    private static final int DEFAULT_SIZE = 1028;
    private static final int BITS_PER_LONG = 63;
    private final AtomicLong count = new AtomicLong();
    private final AtomicLongArray values;

    /**
     * 创建了一个由1028个元素组成的UniformReservoir对象，
     * 假设正态分布，它提供99.9％的置信度和5％的误差范围。
     */
    public UniformReservoir() {
        this(DEFAULT_SIZE);
    }

    /**
     * 构造函数，指定对象元素组成个数
     * 数组初始化0
     */
    public UniformReservoir(int size) {
        this.values = new AtomicLongArray(size);
        for (int i = 0; i < values.length(); i++) {
            values.set(i, 0);
        }
        count.set(0);  // 当前库的值数量初始为0
    }

    /**
     * 返回值个数，如果count值超过了默认大小，就返回默认大小；反之返回count
     * @return
     */
    @Override
    public int size() {
        final long c = count.get();
        if (c > values.length()) {
            return values.length();
        }
        return (int) c;
    }

    /**
     * 更新库值
     * 1、count++
     * 2、判断count小于默认大小
     *      是：向values中(count-1)位置赋值value
     *      否：随机获取一个值r, r in [0,n)，向values中r位置赋值value（相当于会覆盖一个值）（随即覆盖）
     * @param value
     */
    @Override
    public void update(long value) {
        final long c = count.incrementAndGet();
        if (c <= values.length()) {
            values.set((int) c - 1, value);
        } else {
            final long r = nextLong(c);
            if (r < values.length()) {
                values.set((int) r, value);
            }
        }
    }

    /**
     * 获取一个伪随机长整型，范围[0,n)
     */
    private static long nextLong(long n) {
        long bits, val;
        do {
            bits = ThreadLocalRandom.current().nextLong() & (~(1L << BITS_PER_LONG));
            val = bits % n;
        } while (bits - val + (n - 1) < 0L);
        return val;
    }

    @Override
    public Snapshot getSnapshot() {
        final int s = size();
        final List<Long> copy = new ArrayList<Long>(s);
        for (int i = 0; i < s; i++) {
            copy.add(values.get(i));
        }
        return new Snapshot(copy);
    }
}
