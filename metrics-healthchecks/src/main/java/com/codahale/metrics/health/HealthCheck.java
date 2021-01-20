package com.codahale.metrics.health;

/**
 * 应用程序组件的运行状况检查。(健康检查)
 */
public abstract class HealthCheck {
    /**
     * 运行HealthCheck的结果。它可以是健康的（带有可选消息）或不健康的（带有错误消息或抛出异常）。
     */
    public static class Result {
        /**
         * 定义了一个健康对象HEALTHY
         */
        private static final Result HEALTHY = new Result(true, null, null);
        private static final int PRIME = 31;

        /**
         * 返回健康的{@link Result}，没有附加消息。
         */
        public static Result healthy() {
            return HEALTHY;
        }

        /**
         * 返回带有附加消息的健康{@link Result}。
         */
        public static Result healthy(String message) {
            return new Result(true, message, null);
        }

        /**
         * 返回带有格式信息的健康{@link Result}
         *
         * @param message a message format
         * @param args    the arguments apply to the message format
         * @return a healthy {@link Result} with an additional message
         * @see String#format(String, Object...)
         */
        public static Result healthy(String message, Object... args) {
            return healthy(String.format(message, args));
        }

        /**
         * 返回带有附加消息的不健康{@link Result}。
         */
        public static Result unhealthy(String message) {
            return new Result(false, message, null);
        }

        /**
         * 返回带有格式信息的不健康{@link Result}
         *
         * @param message a message format
         * @param args    the arguments apply to the message format
         * @return an unhealthy {@link Result} with an additional message
         * @see String#format(String, Object...)
         */
        public static Result unhealthy(String message, Object... args) {
            return unhealthy(String.format(message, args));
        }

        /**
         * 返回带有异常信息和异常的不健康{@link Result}
         */
        public static Result unhealthy(Throwable error) {
            return new Result(false, error.getMessage(), error);
        }

        private final boolean healthy;
        private final String message;
        private final Throwable error;

        /**
         * 构造函数
         * @param isHealthy
         * @param message
         * @param error
         */
        private Result(boolean isHealthy, String message, Throwable error) {
            this.healthy = isHealthy;
            this.message = message;
            this.error = error;
        }

        /**
         * Returns {@code true} if the result indicates the component is healthy; {@code false}
         * otherwise.
         *
         * @return {@code true} if the result indicates the component is healthy
         */
        public boolean isHealthy() {
            return healthy;
        }

        /**
         * Returns any additional message for the result, or {@code null} if the result has no
         * message.
         *
         * @return any additional message for the result, or {@code null}
         */
        public String getMessage() {
            return message;
        }

        /**
         * Returns any exception for the result, or {@code null} if the result has no exception.
         *
         * @return any exception for the result, or {@code null}
         */
        public Throwable getError() {
            return error;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            final Result result = (Result) o;
            return healthy == result.healthy &&
                    !(error != null ? !error.equals(result.error) : result.error != null) &&
                    !(message != null ? !message.equals(result.message) : result.message != null);
        }

        @Override
        public int hashCode() {
            int result = (healthy ? 1 : 0);
            result = PRIME * result + (message != null ? message.hashCode() : 0);
            result = PRIME * result + (error != null ? error.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder("Result{isHealthy=");
            builder.append(healthy);
            if (message != null) {
                builder.append(", message=").append(message);
            }
            if (error != null) {
                builder.append(", error=").append(error);
            }
            builder.append('}');
            return builder.toString();
        }
    }

    /**
     * Perform a check of the application component.
     *
     * @return if the component is healthy, a healthy {@link Result}; otherwise, an unhealthy {@link
     *         Result} with a descriptive error message or exception
     * @throws Exception if there is an unhandled error during the health check; this will result in
     *                   a failed health check
     */
    protected abstract Result check() throws Exception;

    /**
     * 执行运行状况检查，捕获并处理check()引发的所有异常。
     */
    public Result execute() {
        try {
            return check();
        } catch (Exception e) {
            return Result.unhealthy(e);
        }
    }
}
