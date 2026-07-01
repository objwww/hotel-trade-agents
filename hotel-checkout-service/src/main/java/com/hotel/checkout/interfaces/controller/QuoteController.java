package com.hotel.checkout.interfaces.controller;

import com.hotel.checkout.application.command.QuoteCreateCommand;
import com.hotel.checkout.application.service.QuoteAppService;
import com.hotel.checkout.interfaces.request.QuoteCreateRequest;
import com.hotel.checkout.interfaces.response.QuoteCreateResponse;
import com.hotel.checkout.interfaces.response.QuoteDetailResponse;
import com.hotel.common.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout/quotes")
public class QuoteController {

    private final QuoteAppService quoteAppService;

    @PostMapping
    public ApiResponse<QuoteCreateResponse> createQuote(
            @Valid @RequestBody QuoteCreateRequest request
    ) {
        QuoteCreateCommand command = new QuoteCreateCommand(
                request.hotelId(),
                request.sellerId(),
                request.roomTypeId(),
                request.ratePlanId(),
                request.checkInDate(),
                request.checkOutDate(),
                request.roomCount()
        );

        return ApiResponse.success(quoteAppService.createQuote(command));
    }

    @GetMapping("/{quoteToken}")
    public ApiResponse<QuoteDetailResponse> getQuote(
            @PathVariable("quoteToken") String quoteToken
    ) {
        return ApiResponse.success(quoteAppService.getQuote(quoteToken));
    }
}