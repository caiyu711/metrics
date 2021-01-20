package com.codahale.metrics;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * RatioGauge类单元测试
 */
public class RatioGaugeTest {

    /**
     * RatioGauge.toString()方法，返回形式：分子:分母
     */
    @Test
    public void ratiosAreHumanReadable() throws Exception {
        final RatioGauge.Ratio ratio = RatioGauge.Ratio.of(100, 200);

        assertThat(ratio.toString())
                .isEqualTo("100.0:200.0");
    }

    /**
     * value = 2.0 / 4.0 = 0.5
     */
    @Test
    public void calculatesTheRatioOfTheNumeratorToTheDenominator() throws Exception {
        final RatioGauge regular = new RatioGauge() {
            /**
             * 实现RatioGauge.getRatio()方法，默认分子为2，分母为4
             */
            @Override
            protected Ratio getRatio() {
                return RatioGauge.Ratio.of(2, 4);
            }
        };

        assertThat(regular.getValue())
                .isEqualTo(0.5);
    }

    /**
     * 分母为0时返回NaN
     */
    @Test
    public void handlesDivideByZeroIssues() throws Exception {
        final RatioGauge divByZero = new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(100, 0);
            }
        };

        assertThat(divByZero.getValue())
                .isNaN();
    }

    /**
     * 分母为INFINITY时返回NaN
     */
    @Test
    public void handlesInfiniteDenominators() throws Exception {
        final RatioGauge infinite = new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(10, Double.POSITIVE_INFINITY);
            }
        };

        assertThat(infinite.getValue())
                .isNaN();
    }

    /**
     * 分母为NaN时返回NaN
     */
    @Test
    public void handlesNaNDenominators() throws Exception {
        final RatioGauge nan = new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(10, Double.NaN);
            }
        };

        assertThat(nan.getValue())
                .isNaN();
    }
}
