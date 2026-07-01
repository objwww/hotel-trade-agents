package com.hotel.trade.infrastructure.client.supply.request;

import java.time.LocalDate;

public record InventoryFreezeRequest(
        String freezeToken,
        String orderNo,
        String ratePlanId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomCount
) {
}