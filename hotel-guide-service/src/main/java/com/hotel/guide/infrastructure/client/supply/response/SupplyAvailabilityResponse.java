package com.hotel.guide.infrastructure.client.supply.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SupplyAvailabilityResponse(
        String ratePlanId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount,
        Boolean available,
        Integer maxBookableRoomCount,
        BigDecimal totalAmount,
        String currency,
        List<SupplyDailyAvailabilityResponse> dailyPrices
) {
}