package com.hotel.trade.infrastructure.client.supply.request;

public record InventoryConfirmRequest(
        String freezeToken,
        String orderNo
) {
}