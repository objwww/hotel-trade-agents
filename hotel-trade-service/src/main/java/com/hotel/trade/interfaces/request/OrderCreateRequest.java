package com.hotel.trade.interfaces.request;

import jakarta.validation.constraints.NotBlank;

public record OrderCreateRequest(
        @NotBlank String quoteToken,
        @NotBlank String clientOrderNo,
        String contactName,
        String contactPhone
) {
}