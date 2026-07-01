package com.hotel.admin.infrastructure.persistence.mapper;

import lombok.Data;

@Data
public class PerformanceMetricDTO {

    private String operationType;

    private Long totalCount;

    private Long successCount;

    private Long failureCount;

    private Double avgCostMs;

    private Long maxCostMs;
}