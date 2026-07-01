package com.hotel.trade.interfaces.response;

import java.time.LocalDateTime;

public record OrderPaySuccessResponse(
        String orderNo,
        String orderStatus,
        String freezeToken,
        String paymentNo,
        LocalDateTime paidAt
) {
}