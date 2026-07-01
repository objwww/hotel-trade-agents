package com.hotel.checkout.infrastructure.client.supply.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SupplyDailyAvailabilityResponse(
        LocalDate date,
        BigDecimal price,
        Integer availableCount
) {
}