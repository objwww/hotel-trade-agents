package com.hotel.trade.interfaces.request;

public record OrderCancelRequest(
        String cancelReason
) {
}