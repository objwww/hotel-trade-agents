package com.hotel.guide.interfaces.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GuideHotelSearchItemResponse(
        String hotelId,
        String hotelName,
        String country,
        String city,
        String address,
        Integer starLevel,

        String roomTypeId,
        String roomTypeName,
        String bedType,
        Integer maxGuests,
        String area,

        String ratePlanId,
        String ratePlanName,
        String breakfastType,

        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount,
        Boolean available,
        Integer maxBookableRoomCount,
        BigDecimal lowestAmount,
        String currency,

        String inventoryTag,
        String breakfastTag,
        String cancelTag,
        String dataFreshness
) {
}