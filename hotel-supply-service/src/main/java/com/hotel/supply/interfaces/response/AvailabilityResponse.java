package com.hotel.supply.interfaces.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AvailabilityResponse(
        String ratePlanId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount,
        Boolean available,
        Integer maxBookableRoomCount,
        BigDecimal totalAmount,
        String currency,
        List<DailyAvailabilityResponse> dailyPrices
) {
}