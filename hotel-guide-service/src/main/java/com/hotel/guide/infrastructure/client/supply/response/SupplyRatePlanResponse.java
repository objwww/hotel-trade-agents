package com.hotel.guide.infrastructure.client.supply.response;

public record SupplyRatePlanResponse(
        String ratePlanId,
        String hotelId,
        String sellerId,
        String roomTypeId,
        String ratePlanName,
        String breakfastType,
        String status
) {
}