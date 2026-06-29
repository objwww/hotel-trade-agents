package com.hotel.supply.interfaces.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyAvailabilityResponse(
        LocalDate date,
        BigDecimal price,
        Integer availableCount
) {
}