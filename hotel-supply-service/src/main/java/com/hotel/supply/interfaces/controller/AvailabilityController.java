package com.hotel.supply.interfaces.controller;

import com.hotel.common.api.ApiResponse;
import com.hotel.supply.application.query.AvailabilityQuery;
import com.hotel.supply.application.service.SupplyQueryAppService;
import com.hotel.supply.interfaces.response.AvailabilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supply/rate-plans")
public class AvailabilityController {

    private final SupplyQueryAppService supplyQueryAppService;

    @GetMapping("/{ratePlanId}/availability")
    public ApiResponse<AvailabilityResponse> availability(
            @PathVariable("ratePlanId") String ratePlanId,

            @RequestParam("checkInDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkInDate,

            @RequestParam("checkOutDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkOutDate,

            @RequestParam("roomCount") Integer roomCount
    ) {
        AvailabilityQuery query = new AvailabilityQuery(
                ratePlanId,
                checkInDate,
                checkOutDate,
                roomCount
        );

        return ApiResponse.success(supplyQueryAppService.queryAvailability(query));
    }
}