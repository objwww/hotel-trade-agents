package com.hotel.trade.interfaces.response;

import java.time.LocalDateTime;

public record OrderCancelResponse(
        String orderNo,
        String orderStatus,
        String freezeToken,
        String cancelReason,
        LocalDateTime cancelAt
) {
}