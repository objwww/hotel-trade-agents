package com.hotel.supply.interfaces.response;

public record InventoryConfirmResponse(
        String freezeToken,
        String orderNo,
        String ratePlanId,
        String freezeStatus,
        Integer roomCount
) {
}