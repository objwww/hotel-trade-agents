package com.hotel.supply.interfaces.request;

public record InventoryConfirmRequest(
        String freezeToken,
        String orderNo
) {
}