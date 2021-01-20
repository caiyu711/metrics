package com.codahale.metrics.jdbi.strategies;

import org.skife.jdbi.v2.StatementContext;

/**
 * Interface for strategies to statement contexts to metric names.
 * 策略接口，用于声明度量名称的上下文。
 */
public interface StatementNameStrategy {
    String getStatementName(StatementContext statementContext);
}
