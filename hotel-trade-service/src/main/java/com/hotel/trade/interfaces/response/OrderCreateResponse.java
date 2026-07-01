package com.hotel.trade.interfaces.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderCreateResponse(
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
        String freezeToken
) {
}