package com.hotel.trade.infrastructure.client.supply.request;

public record InventoryReleaseRequest(
        String freezeToken,
        String orderNo
) {
}