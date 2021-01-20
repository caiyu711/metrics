package com.codahale.metrics;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * DerivativeGauge单元测试
 */
public class DerivativeGaugeTest {
    /**
     * 创建一个Gauge对象，数据类型为String
     */
    private final Gauge<String> gauge1 = new Gauge<String>() {
        @Override
        public String getValue() {
            return "woo";
        }
    };
    /**
     * 该传递对象类型为String(对应F，即base)
     * 要被传递的对象类型为Integer（对应T）
     * 实现了transform()方法，返回字符串长度
     */
    private final Gauge<Integer> gauge2 = new DerivativeGauge<String, Integer>(gauge1) {
        @Override
        protected Integer transform(String value) {
            return value.length();
        }
    };

    @Test
    public void returnsATransformedValue() throws Exception {
        assertThat(gauge2.getValue())
                .isEqualTo(3);
    }
}
