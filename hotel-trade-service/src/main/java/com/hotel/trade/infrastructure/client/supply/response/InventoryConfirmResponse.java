package com.hotel.trade.infrastructure.client.supply.response;

public record InventoryConfirmResponse(
        String freezeToken,
        String orderNo,
        String ratePlanId,
        String freezeStatus,
        Integer roomCount
) {
}