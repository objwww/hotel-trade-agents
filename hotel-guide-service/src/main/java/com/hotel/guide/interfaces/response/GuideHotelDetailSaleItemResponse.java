package com.hotel.guide.interfaces.response;

import java.math.BigDecimal;

public record GuideHotelDetailSaleItemResponse(
        String roomTypeId,
        String roomTypeName,
        String bedType,
        Integer maxGuests,
        String area,

        String ratePlanId,
        String ratePlanName,
        String breakfastType,

        Boolean available,
        Integer maxBookableRoomCount,
        BigDecimal totalAmount,
        String currency,

        String inventoryTag,
        String breakfastTag,
        String cancelTag,
        String dataFreshness
) {
}