package com.hotel.supply.application.service;

import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.supply.application.assembler.AvailabilityAssembler;
import com.hotel.supply.application.query.AvailabilityQuery;
import com.hotel.supply.domain.model.aggregate.DailyInventory;
import com.hotel.supply.domain.model.aggregate.Hotel;
import com.hotel.supply.domain.model.aggregate.RatePlan;
import com.hotel.supply.domain.model.entity.DailyRate;
import com.hotel.supply.domain.model.entity.RoomType;
import com.hotel.supply.domain.model.valueobject.Availability;
import com.hotel.supply.domain.model.valueobject.DateRange;
import com.hotel.supply.domain.repository.DailyInventoryRepository;
import com.hotel.supply.domain.repository.DailyRateRepository;
import com.hotel.supply.domain.repository.HotelRepository;
import com.hotel.supply.domain.repository.RatePlanRepository;
import com.hotel.supply.domain.repository.RoomTypeRepository;
import com.hotel.supply.domain.service.AvailabilityDomainService;
import com.hotel.supply.interfaces.response.AvailabilityResponse;
import com.hotel.supply.interfaces.response.HotelResponse;
import com.hotel.supply.interfaces.response.RatePlanResponse;
import com.hotel.supply.interfaces.response.RoomTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplyQueryAppService {

    private final HotelRepository hotelRepository;

    private final RoomTypeRepository roomTypeRepository;

    private final RatePlanRepository ratePlanRepository;

    private final DailyRateRepository dailyRateRepository;

    private final DailyInventoryRepository dailyInventoryRepository;

    private final AvailabilityDomainService availabilityDomainService;

    private final AvailabilityAssembler availabilityAssembler;

    public HotelResponse getHotel(String hotelId) {
        Hotel hotel = hotelRepository.findByHotelId(hotelId)
                .orElseThrow(() -> new BizException(ErrorCode.HOTEL_NOT_FOUND));

        return availabilityAssembler.toHotelResponse(hotel);
    }

    public List<RoomTypeResponse> listRoomTypes(String hotelId) {
        hotelRepository.findByHotelId(hotelId)
                .orElseThrow(() -> new BizException(ErrorCode.HOTEL_NOT_FOUND));

        List<RoomType> roomTypes = roomTypeRepository.findByHotelId(hotelId);

        return roomTypes.stream()
                .map(availabilityAssembler::toRoomTypeResponse)
                .toList();
    }

    public List<RatePlanResponse> listRatePlans(String hotelId) {
        hotelRepository.findByHotelId(hotelId)
                .orElseThrow(() -> new BizException(ErrorCode.HOTEL_NOT_FOUND));

        List<RatePlan> ratePlans = ratePlanRepository.findByHotelId(hotelId);

        return ratePlans.stream()
                .map(availabilityAssembler::toRatePlanResponse)
                .toList();
    }

    public AvailabilityResponse queryAvailability(AvailabilityQuery query) {
        DateRange dateRange = DateRange.of(query.checkInDate(), query.checkOutDate());

        RatePlan ratePlan = ratePlanRepository.findByRatePlanId(query.ratePlanId())
                .orElseThrow(() -> new BizException(ErrorCode.RATE_PLAN_NOT_FOUND));

        List<DailyRate> dailyRates = dailyRateRepository.findByRatePlanIdAndDateRange(
                query.ratePlanId(),
                dateRange
        );

        List<DailyInventory> dailyInventories = dailyInventoryRepository.findByRatePlanIdAndDateRange(
                query.ratePlanId(),
                dateRange
        );

        Availability availability = availabilityDomainService.calculate(
                ratePlan,
                dateRange,
                dailyRates,
                dailyInventories,
                query.roomCount()
        );

        return availabilityAssembler.toResponse(availability);
    }
}