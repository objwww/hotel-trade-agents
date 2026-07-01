package com.hotel.guide.application.query;

import java.time.LocalDate;

public record GuideHotelDetailQuery(
        String hotelId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount
) {
}