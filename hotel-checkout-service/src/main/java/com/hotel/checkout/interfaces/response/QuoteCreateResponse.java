package com.hotel.checkout.interfaces.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record QuoteCreateResponse(
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
        String currency
) {
}