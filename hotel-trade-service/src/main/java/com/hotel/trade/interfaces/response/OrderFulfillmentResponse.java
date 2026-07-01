package com.hotel.trade.interfaces.response;

import java.time.LocalDateTime;

public record OrderFulfillmentResponse(
        String orderNo,
        String orderStatus,
        String fulfillmentNo,
        String supplierConfirmNo,
        LocalDateTime confirmedAt
) {
}