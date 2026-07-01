package com.hotel.admin.interfaces.response;

import java.math.BigDecimal;

public record MonitorOverviewResponse(
        Long totalOrderCount,
        Long successOrderCount,
        Long failedOrderCount,
        Long pendingPaymentCount,
        Long paidCount,
        Long confirmedCount,
        Long cancelledCount,

        BigDecimal totalSuccessAmount,

        Long apiSuccessCount,
        Long apiFailureCount,
        Double avgCostMs
) {
}