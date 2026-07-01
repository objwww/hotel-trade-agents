package com.hotel.guide.application.query;

import java.time.LocalDate;

public record GuideSearchQuery(
        String city,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount
) {
}