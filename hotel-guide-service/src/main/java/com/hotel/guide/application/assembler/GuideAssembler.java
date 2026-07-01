package com.hotel.guide.application.assembler;

import com.hotel.guide.domain.model.GuideOffer;
import com.hotel.guide.infrastructure.client.supply.response.SupplyAvailabilityResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyHotelResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyRatePlanResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyRoomTypeResponse;
import com.hotel.guide.interfaces.response.GuideHotelDetailResponse;
import com.hotel.guide.interfaces.response.GuideHotelDetailSaleItemResponse;
import com.hotel.guide.interfaces.response.GuideHotelSearchItemResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class GuideAssembler {

    public GuideHotelSearchItemResponse toSearchItem(
            GuideOffer offer,
            SupplyHotelResponse hotel,
            SupplyRoomTypeResponse roomType,
            SupplyRatePlanResponse ratePlan,
            SupplyAvailabilityResponse availability
    ) {
        return new GuideHotelSearchItemResponse(
                hotel.hotelId(),
                hotel.hotelName(),
                hotel.country(),
                hotel.city(),
                hotel.address(),
                hotel.starLevel(),

                roomType.roomTypeId(),
                roomType.roomTypeName(),
                roomType.bedType(),
                roomType.maxGuests(),
                roomType.area(),

                ratePlan.ratePlanId(),
                ratePlan.ratePlanName(),
                ratePlan.breakfastType(),

                availability.checkInDate(),
                availability.checkOutDate(),
                availability.roomCount(),
                availability.available(),
                availability.maxBookableRoomCount(),
                availability.totalAmount(),
                availability.currency(),

                offer.getInventoryTag(),
                offer.getBreakfastTag(),
                offer.getCancelTag(),
                offer.getDataFreshness()
        );
    }

    public GuideHotelDetailSaleItemResponse toDetailSaleItem(
            GuideOffer offer,
            SupplyRoomTypeResponse roomType,
            SupplyRatePlanResponse ratePlan,
            SupplyAvailabilityResponse availability
    ) {
        return new GuideHotelDetailSaleItemResponse(
                roomType.roomTypeId(),
                roomType.roomTypeName(),
                roomType.bedType(),
                roomType.maxGuests(),
                roomType.area(),

                ratePlan.ratePlanId(),
                ratePlan.ratePlanName(),
                ratePlan.breakfastType(),

                availability.available(),
                availability.maxBookableRoomCount(),
                availability.totalAmount(),
                availability.currency(),

                offer.getInventoryTag(),
                offer.getBreakfastTag(),
                offer.getCancelTag(),
                offer.getDataFreshness()
        );
    }

    public GuideHotelDetailResponse toHotelDetail(
            SupplyHotelResponse hotel,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Integer roomCount,
            List<GuideHotelDetailSaleItemResponse> saleItems
    ) {
        return new GuideHotelDetailResponse(
                hotel.hotelId(),
                hotel.hotelName(),
                hotel.country(),
                hotel.city(),
                hotel.address(),
                hotel.starLevel(),

                checkInDate,
                checkOutDate,
                roomCount,

                saleItems.size(),
                saleItems
        );
    }
}