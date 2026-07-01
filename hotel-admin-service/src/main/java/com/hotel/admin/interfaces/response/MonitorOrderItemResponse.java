package com.hotel.admin.interfaces.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MonitorOrderItemResponse(
        String orderNo,
        String clientOrderNo,
        String orderStatus,
        BigDecimal orderAmount,
        String currency,
        String sourceTraceId,
        String failureTraceId,
        String cancelReason,
        LocalDateTime cancelAt,
        LocalDateTime createdAt
) {
}