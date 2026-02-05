package org.example.Model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PerformanceMetrics implements Serializable {
    private static final long serialVersionUID = 1L;

    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final AtomicInteger successfulRequests = new AtomicInteger(0);
    private final AtomicInteger failedRequests = new AtomicInteger(0);
    private final AtomicLong totalResponseTime = new AtomicLong(0);
    private final long startTime;

    public PerformanceMetrics() {
        this.startTime = System.currentTimeMillis();
    }

    public void recordRequest(long responseTimeMs, boolean success) {
        totalRequests.incrementAndGet();
        totalResponseTime.addAndGet(responseTimeMs);

        if (success) {
            successfulRequests.incrementAndGet();
        } else {
            failedRequests.incrementAndGet();
        }
    }

    public double getAverageResponseTime() {
        int total = totalRequests.get();
        if (total == 0) return 0;
        return (double) totalResponseTime.get() / total;
    }

    public double getThroughput() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed == 0) return 0;
        return (double) totalRequests.get() / (elapsed / 1000.0);
    }

    public int getTotalRequests() {
        return totalRequests.get();
    }

    public int getSuccessfulRequests() {
        return successfulRequests.get();
    }

    public int getFailedRequests() {
        return failedRequests.get();
    }

    public double getSuccessRate() {
        int total = totalRequests.get();
        if (total == 0) return 0;
        return (double) successfulRequests.get() / total * 100;
    }

    public long getElapsedTimeSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    @Override
    public String toString() {
        return String.format(
            "PerformanceMetrics{total=%d, success=%d, failed=%d, avgResponseTime=%.2fms, throughput=%.2f req/s, successRate=%.2f%%}",
            getTotalRequests(),
            getSuccessfulRequests(),
            getFailedRequests(),
            getAverageResponseTime(),
            getThroughput(),
            getSuccessRate()
        );
    }

    public String toCSV() {
        return String.format(
            "%d,%.2f,%.2f,%.2f,%d,%d,%d",
            getElapsedTimeSeconds(),
            getAverageResponseTime(),
            getThroughput(),
            getSuccessRate(),
            getTotalRequests(),
            getSuccessfulRequests(),
            getFailedRequests()
        );
    }
}
