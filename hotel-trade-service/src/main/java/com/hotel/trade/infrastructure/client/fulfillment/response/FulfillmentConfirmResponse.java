package com.hotel.trade.infrastructure.client.fulfillment.response;

public record FulfillmentConfirmResponse(
        String fulfillmentNo,
        String orderNo,
        String fulfillmentStatus,
        String supplierConfirmNo
) {
}