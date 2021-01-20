package com.codahale.metrics;

/**
 *  Gauge的一个实现类，其值是由另一个Gauge对象传递的
 *  F 为传递对象的类型
 *  T 为被传递的对象类型
 */
public abstract class DerivativeGauge<F, T> implements Gauge<T> {
    private final Gauge<F> base; // 传递的对象

    /**
     * 构造函数
     */
    protected DerivativeGauge(Gauge<F> base) {
        this.base = base;
    }

    /**
     * 实现Gauge.getValue()方法
     */
    @Override
    public T getValue() {
        return transform(base.getValue());
    }

    /**
     * 将base的value值转换成该gauge对象值
     * 创建DerivativeGauge对象需要实现该方法
     */
    protected abstract T transform(F value);
}
