package com.hotel.supply.interfaces.response;

public record HotelResponse(
        String hotelId,
        String hotelName,
        String country,
        String city,
        String address,
        Integer starLevel,
        String status
) {
}