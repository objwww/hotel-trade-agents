package com.hotel.supply.interfaces.response;

public record RoomTypeResponse(
        String roomTypeId,
        String hotelId,
        String roomTypeName,
        String bedType,
        Integer maxGuests,
        String area,
        String status
) {
}