package com.codahale.metrics;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

/**
 * Gauge的一个实现类，用来计算一个值与另一个值的比（Double）
 * 如果分母为0、或NaN、或INFINITY，返回NaN
 */
public abstract class RatioGauge implements Gauge<Double> {
    /**
     * Radio类
     */
    public static class Ratio {
        /**
         * 创建一个Radio对象，指定分子和分母
         */
        public static Ratio of(double numerator, double denominator) {
            return new Ratio(numerator, denominator);
        }

        private final double numerator; // 分子
        private final double denominator; // 分母

        /**
         * 构造函数
         */
        private Ratio(double numerator, double denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public double getValue() {
            final double d = denominator;
            if (isNaN(d) || isInfinite(d) || d == 0) {
                return Double.NaN;
            }
            return numerator / d;
        }

        @Override
        public String toString() {
            return numerator + ":" + denominator;
        }
    }

    /**
     * 获取Radio对象，定义RatioGauge对象时候需要实现
     */
    protected abstract Ratio getRatio();

    /**
     * 实现Gauge.getValue()
     * @return
     */
    @Override
    public Double getValue() {
        return getRatio().getValue();
    }
}
