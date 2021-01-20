package com.codahale.metrics.httpclient;

import org.apache.http.HttpRequest;

/**
 * http请求中metrics名称策略（抽象接口）
 */
public interface HttpClientMetricNameStrategy {
    String getNameFor(String name, HttpRequest request);
}
