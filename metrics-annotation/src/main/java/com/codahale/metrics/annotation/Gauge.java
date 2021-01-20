package com.codahale.metrics.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个注释，用于将一个带注释对象的方法标记为Gauge。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface Gauge {
    /**
     * The gauge's name.
     */
    String name() default "";

    /**
     * 如果{@code true}，则使用给定名称作为绝对名称。如果{@code false}，则使用相对于带注释的类的给定名称。
     */
    boolean absolute() default false;
}
