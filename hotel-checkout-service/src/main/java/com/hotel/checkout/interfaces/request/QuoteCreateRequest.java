package com.hotel.checkout.interfaces.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record QuoteCreateRequest(
        @NotBlank String hotelId,
        @NotBlank String sellerId,
        @NotBlank String roomTypeId,
        @NotBlank String ratePlanId,
        @NotNull LocalDate checkInDate,
        @NotNull LocalDate checkOutDate,
        @NotNull @Positive Integer roomCount
) {
}