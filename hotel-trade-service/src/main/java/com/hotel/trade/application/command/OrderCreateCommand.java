package com.hotel.trade.application.command;

public record OrderCreateCommand(
        String quoteToken,
        String clientOrderNo,
        String contactName,
        String contactPhone
) {
}