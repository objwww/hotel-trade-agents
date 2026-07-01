package com.hotel.guide.interfaces.response;

import java.time.LocalDate;
import java.util.List;

public record GuideHotelDetailResponse(
        String hotelId,
        String hotelName,
        String country,
        String city,
        String address,
        Integer starLevel,

        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount,

        Integer totalOfferCount,
        List<GuideHotelDetailSaleItemResponse> saleItems
) {
}