package com.tyss.optimize.performance.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PerformanceTestResponse {
    private long totalRequests;
    private double throughput;
    private long avgResponseTime;
    private long minResponseTime;
    private long maxResponseTime;
    private long errorCount;
    private double errorPercentage;

    // Getters and setters
}
