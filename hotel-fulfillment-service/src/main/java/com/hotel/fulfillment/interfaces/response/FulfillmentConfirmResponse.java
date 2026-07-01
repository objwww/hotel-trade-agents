package com.hotel.fulfillment.interfaces.response;

public record FulfillmentConfirmResponse(
        String fulfillmentNo,
        String orderNo,
        String fulfillmentStatus,
        String supplierConfirmNo
) {
}