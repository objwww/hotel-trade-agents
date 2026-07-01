package com.hotel.checkout.application.assembler;

import com.hotel.checkout.domain.model.QuoteSnapshot;
import com.hotel.checkout.interfaces.response.QuoteCreateResponse;
import com.hotel.checkout.interfaces.response.QuoteDetailResponse;
import org.springframework.stereotype.Component;

@Component
public class QuoteAssembler {

    public QuoteCreateResponse toCreateResponse(QuoteSnapshot quoteSnapshot) {
        return new QuoteCreateResponse(
                quoteSnapshot.getQuoteToken(),
                quoteSnapshot.getExpireAt(),

                quoteSnapshot.getHotelId(),
                quoteSnapshot.getSellerId(),
                quoteSnapshot.getRoomTypeId(),
                quoteSnapshot.getRatePlanId(),

                quoteSnapshot.getCheckInDate(),
                quoteSnapshot.getCheckOutDate(),
                quoteSnapshot.getRoomCount(),

                quoteSnapshot.getAvailable(),
                quoteSnapshot.getMaxBookableRoomCount(),
                quoteSnapshot.getTotalAmount(),
                quoteSnapshot.getCurrency()
        );
    }

    public QuoteDetailResponse toDetailResponse(QuoteSnapshot quoteSnapshot) {
        return new QuoteDetailResponse(
                quoteSnapshot.getQuoteToken(),
                quoteSnapshot.getExpireAt(),

                quoteSnapshot.getHotelId(),
                quoteSnapshot.getSellerId(),
                quoteSnapshot.getRoomTypeId(),
                quoteSnapshot.getRatePlanId(),

                quoteSnapshot.getCheckInDate(),
                quoteSnapshot.getCheckOutDate(),
                quoteSnapshot.getRoomCount(),

                quoteSnapshot.getAvailable(),
                quoteSnapshot.getMaxBookableRoomCount(),
                quoteSnapshot.getTotalAmount(),
                quoteSnapshot.getCurrency(),

                quoteSnapshot.getQuoteStatus()
        );
    }
}