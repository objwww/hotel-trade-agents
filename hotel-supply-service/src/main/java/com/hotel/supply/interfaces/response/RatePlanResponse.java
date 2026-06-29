package com.hotel.supply.interfaces.response;

public record RatePlanResponse(
        String ratePlanId,
        String hotelId,
        String sellerId,
        String roomTypeId,
        String ratePlanName,
        String breakfastType,
        String status
) {
}