package com.hotel.supply.interfaces.request;

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