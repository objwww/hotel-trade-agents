package com.hotel.guide.interfaces.controller;

import com.hotel.common.api.ApiResponse;
import com.hotel.guide.application.query.GuideSearchQuery;
import com.hotel.guide.application.service.GuideSearchAppService;
import com.hotel.guide.interfaces.response.GuideSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guide/hotels")
public class GuideSearchController {

    private final GuideSearchAppService guideSearchAppService;

    @GetMapping("/search")
    public ApiResponse<GuideSearchResponse> search(
            @RequestParam("city") String city,

            @RequestParam("checkInDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkInDate,

            @RequestParam("checkOutDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkOutDate,

            @RequestParam("roomCount") Integer roomCount
    ) {
        GuideSearchQuery query = new GuideSearchQuery(
                city,
                checkInDate,
                checkOutDate,
                roomCount
        );

        return ApiResponse.success(guideSearchAppService.search(query));
    }
}