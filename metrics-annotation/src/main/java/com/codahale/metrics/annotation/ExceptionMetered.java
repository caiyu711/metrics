package com.codahale.metrics.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个注释，用于将一个带注释对象的方法标记为Meter。
 * 指定cause时，当方法抛出指定异常，meter会被标记
 * 不知道cause时，方法抛出任何异常都会被标记
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionMetered {
    /**
     * 名称的默认后缀
     */
    String DEFAULT_NAME_SUFFIX = "exceptions";

    /**
     * meter名称
     * 如果不指定，仪表将根据其装饰的方法和默认后缀来命名。
     */
    String name() default "";

    /**
     * If {@code true}, use the given name as an absolute name. If {@code false}, use the given name
     * relative to the annotated class.
     */
    boolean absolute() default false;

    /**
     * The type of exceptions that the meter will catch and count.
     */
    Class<? extends Throwable> cause() default Exception.class;
}
