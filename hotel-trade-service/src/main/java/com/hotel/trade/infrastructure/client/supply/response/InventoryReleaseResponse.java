package com.hotel.trade.infrastructure.client.supply.response;

public record InventoryReleaseResponse(
        String freezeToken,
        String orderNo,
        String ratePlanId,
        String freezeStatus,
        Integer roomCount
) {
}