package com.hotel.trade.interfaces.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrderDetailResponse(
        String orderNo,
        String quoteToken,
        String clientOrderNo,

        String hotelId,
        String sellerId,
        String roomTypeId,
        String ratePlanId,

        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount,

        BigDecimal orderAmount,
        String currency,

        String orderStatus,

        String paymentNo,
        LocalDateTime paidAt,

        String freezeToken,

        String fulfillmentNo,
        String supplierConfirmNo,
        LocalDateTime confirmedAt,

        String contactName,
        String contactPhone,

        String cancelReason,
        LocalDateTime cancelAt,

        String sourceTraceId
) {
}