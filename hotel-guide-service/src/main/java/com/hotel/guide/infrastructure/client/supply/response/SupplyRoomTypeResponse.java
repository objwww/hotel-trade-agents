package com.hotel.guide.infrastructure.client.supply.response;

public record SupplyRoomTypeResponse(
        String roomTypeId,
        String hotelId,
        String roomTypeName,
        String bedType,
        Integer maxGuests,
        String area,
        String status
) {
}