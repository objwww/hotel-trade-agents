package com.hotel.supply.interfaces.response;

public record InventoryFreezeResponse(
        String freezeToken,
        String orderNo,
        String ratePlanId,
        String freezeStatus,
        Integer roomCount
) {
}