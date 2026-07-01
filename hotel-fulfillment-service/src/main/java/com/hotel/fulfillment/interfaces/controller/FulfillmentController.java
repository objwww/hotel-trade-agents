package com.hotel.fulfillment.interfaces.controller;

import com.hotel.common.api.ApiResponse;
import com.hotel.fulfillment.application.service.FulfillmentConfirmAppService;
import com.hotel.fulfillment.interfaces.request.FulfillmentConfirmRequest;
import com.hotel.fulfillment.interfaces.response.FulfillmentConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fulfillment/orders")
public class FulfillmentController {

    private final FulfillmentConfirmAppService fulfillmentConfirmAppService;

    @PostMapping("/confirm")
    public ApiResponse<FulfillmentConfirmResponse> confirm(
            @RequestBody FulfillmentConfirmRequest request
    ) {
        return ApiResponse.success(fulfillmentConfirmAppService.confirm(request));
    }
}