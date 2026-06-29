package com.hotel.supply.interfaces.controller;

import com.hotel.common.api.ApiResponse;
import com.hotel.supply.application.service.SupplyQueryAppService;
import com.hotel.supply.interfaces.response.HotelResponse;
import com.hotel.supply.interfaces.response.RatePlanResponse;
import com.hotel.supply.interfaces.response.RoomTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supply/hotels")
public class HotelController {

    private final SupplyQueryAppService supplyQueryAppService;

    @GetMapping("/{hotelId}")
    public ApiResponse<HotelResponse> getHotel(
            @PathVariable("hotelId") String hotelId
    ) {
        return ApiResponse.success(supplyQueryAppService.getHotel(hotelId));
    }

    @GetMapping("/{hotelId}/rooms")
    public ApiResponse<List<RoomTypeResponse>> listRoomTypes(
            @PathVariable("hotelId") String hotelId
    ) {
        return ApiResponse.success(supplyQueryAppService.listRoomTypes(hotelId));
    }

    @GetMapping("/{hotelId}/rate-plans")
    public ApiResponse<List<RatePlanResponse>> listRatePlans(
            @PathVariable("hotelId") String hotelId
    ) {
        return ApiResponse.success(supplyQueryAppService.listRatePlans(hotelId));
    }
}