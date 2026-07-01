package com.hotel.guide.interfaces.controller;

import com.hotel.common.api.ApiResponse;
import com.hotel.guide.application.query.GuideHotelDetailQuery;
import com.hotel.guide.application.service.GuideHotelDetailAppService;
import com.hotel.guide.interfaces.response.GuideHotelDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guide/hotels")
public class GuideHotelDetailController {

    private final GuideHotelDetailAppService guideHotelDetailAppService;

    @GetMapping("/{hotelId}/detail")
    public ApiResponse<GuideHotelDetailResponse> detail(
            @PathVariable("hotelId") String hotelId,

            @RequestParam("checkInDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkInDate,

            @RequestParam("checkOutDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkOutDate,

            @RequestParam("roomCount") Integer roomCount
    ) {
        GuideHotelDetailQuery query = new GuideHotelDetailQuery(
                hotelId,
                checkInDate,
                checkOutDate,
                roomCount
        );

        return ApiResponse.success(guideHotelDetailAppService.detail(query));
    }
}