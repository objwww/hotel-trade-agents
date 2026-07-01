package com.hotel.checkout.interfaces.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

//创建订单时要校验报价，所以 Checkout 要支持：
//
//GET /checkout/quotes/{quoteToken}
public record QuoteDetailResponse(
        String quoteToken,
        LocalDateTime expireAt,

        String hotelId,
        String sellerId,
        String roomTypeId,
        String ratePlanId,

        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount,

        Boolean available,
        Integer maxBookableRoomCount,
        BigDecimal totalAmount,
        String currency,

        String quoteStatus
) {
}