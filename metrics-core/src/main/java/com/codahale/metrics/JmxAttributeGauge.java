package com.codahale.metrics;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * 一个Gauge的实现，实现了查询一个MBeanServer对象的属性
 * 有关MBeanServer {@see "https://blog.csdn.net/u013256816/article/details/52800742"}
 */
public class JmxAttributeGauge implements Gauge<Object> {
    private final MBeanServer mBeanServer;
    private final ObjectName objectName;
    private final String attributeName;

    /**
     * 创建一个JmxAttributeGauge对象
     * @param objectName    对象名
     * @param attributeName 对象属性名
     */
    public JmxAttributeGauge(ObjectName objectName, String attributeName) {
        this(ManagementFactory.getPlatformMBeanServer(), objectName, attributeName);
    }

    /**
     * 创建一个JmxAttributeGauge对象
     * @param mBeanServer      mBeanServer对象
     * @param objectName       对象名
     * @param attributeName    对象属性名
     */
    public JmxAttributeGauge(MBeanServer mBeanServer, ObjectName objectName, String attributeName) {
        this.mBeanServer = mBeanServer;
        this.objectName = objectName;
        this.attributeName = attributeName;
    }

    /**
     * 实现了Gauge.getValue()方法
     */
    @Override
    public Object getValue() {
        try {
            return mBeanServer.getAttribute(objectName, attributeName);
        } catch (JMException e) {
            return null;
        }
    }
}
