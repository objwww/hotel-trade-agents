package com.hotel.supply.interfaces.request;

public record InventoryReleaseRequest(
        String freezeToken,
        String orderNo
) {
}