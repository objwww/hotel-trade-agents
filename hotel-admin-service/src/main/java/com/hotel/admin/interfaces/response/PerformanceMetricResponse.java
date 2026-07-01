package com.hotel.admin.interfaces.response;

public record PerformanceMetricResponse(
        String operationType,
        Long totalCount,
        Long successCount,
        Long failureCount,
        Double avgCostMs,
        Long maxCostMs
) {
}