package com.hotel.supply.application.query;

import java.time.LocalDate;

public record AvailabilityQuery(
        String ratePlanId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount
) {
}