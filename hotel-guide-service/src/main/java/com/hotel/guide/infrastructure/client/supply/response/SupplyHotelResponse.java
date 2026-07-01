package com.hotel.guide.infrastructure.client.supply.response;

public record SupplyHotelResponse(
        String hotelId,
        String hotelName,
        String country,
        String city,
        String address,
        Integer starLevel,
        String status
) {
}