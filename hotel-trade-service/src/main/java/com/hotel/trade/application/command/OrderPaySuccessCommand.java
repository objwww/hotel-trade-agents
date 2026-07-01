package com.hotel.trade.application.command;

public record OrderPaySuccessCommand(
        String orderNo,
        String paymentNo
) {
}