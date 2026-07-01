package com.hotel.trade.application.command;

public record OrderCancelCommand(
        String orderNo,
        String cancelReason
) {
}