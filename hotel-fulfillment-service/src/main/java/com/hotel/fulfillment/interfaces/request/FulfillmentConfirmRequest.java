package com.hotel.fulfillment.interfaces.request;

import java.time.LocalDate;

public record FulfillmentConfirmRequest(
        String orderNo,

        String hotelId,
        String sellerId,
        String roomTypeId,
        String ratePlanId,

        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount,

        String contactName,
        String contactPhone
) {
}