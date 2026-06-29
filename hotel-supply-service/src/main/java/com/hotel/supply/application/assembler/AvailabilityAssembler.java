package com.hotel.supply.application.assembler;

import com.hotel.supply.domain.model.aggregate.Hotel;
import com.hotel.supply.domain.model.aggregate.RatePlan;
import com.hotel.supply.domain.model.entity.RoomType;
import com.hotel.supply.domain.model.valueobject.Availability;
import com.hotel.supply.interfaces.response.AvailabilityResponse;
import com.hotel.supply.interfaces.response.DailyAvailabilityResponse;
import com.hotel.supply.interfaces.response.HotelResponse;
import com.hotel.supply.interfaces.response.RatePlanResponse;
import com.hotel.supply.interfaces.response.RoomTypeResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AvailabilityAssembler {

    public AvailabilityResponse toResponse(Availability availability) {
        List<DailyAvailabilityResponse> dailyPrices = availability.getDailyAvailabilities()
                .stream()
                .map(item -> new DailyAvailabilityResponse(
                        item.getDate(),
                        item.getPrice(),
                        item.getAvailableCount()
                ))
                .toList();

        return new AvailabilityResponse(
                availability.getRatePlanId(),
                availability.getCheckInDate(),
                availability.getCheckOutDate(),
                availability.getRoomCount(),
                availability.getAvailable(),
                availability.getMaxBookableRoomCount(),
                availability.getTotalAmount(),
                availability.getCurrency(),
                dailyPrices
        );
    }

    public HotelResponse toHotelResponse(Hotel hotel) {
        return new HotelResponse(
                hotel.getHotelId(),
                hotel.getHotelName(),
                hotel.getCountry(),
                hotel.getCity(),
                hotel.getAddress(),
                hotel.getStarLevel(),
                hotel.getStatus()
        );
    }

    public RoomTypeResponse toRoomTypeResponse(RoomType roomType) {
        return new RoomTypeResponse(
                roomType.getRoomTypeId(),
                roomType.getHotelId(),
                roomType.getRoomTypeName(),
                roomType.getBedType(),
                roomType.getMaxGuests(),
                roomType.getArea(),
                roomType.getStatus()
        );
    }

    public RatePlanResponse toRatePlanResponse(RatePlan ratePlan) {
        return new RatePlanResponse(
                ratePlan.getRatePlanId(),
                ratePlan.getHotelId(),
                ratePlan.getSellerId(),
                ratePlan.getRoomTypeId(),
                ratePlan.getRatePlanName(),
                ratePlan.getBreakfastType(),
                ratePlan.getStatus()
        );
    }
}