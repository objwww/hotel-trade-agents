package com.hotel.guide.interfaces.controller;

import com.hotel.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuideHealthController {

    @GetMapping("/guide/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("hotel-guide-service ok");
    }
}