package com.hotel.supply.interfaces.controller;

import com.hotel.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupplyHealthController {

    @GetMapping("/supply/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("hotel-supply-service ok");
    }
}