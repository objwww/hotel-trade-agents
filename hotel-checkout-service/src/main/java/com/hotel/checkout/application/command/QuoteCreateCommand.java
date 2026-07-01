package com.hotel.checkout.application.command;

import java.time.LocalDate;

public record QuoteCreateCommand(
        String hotelId,
        String sellerId,
        String roomTypeId,
        String ratePlanId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount
) {
}