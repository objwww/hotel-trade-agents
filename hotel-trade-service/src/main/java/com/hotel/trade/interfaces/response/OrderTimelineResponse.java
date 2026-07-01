package com.hotel.trade.interfaces.response;

import java.util.List;

public record OrderTimelineResponse(
        String orderNo,
        Integer total,
        List<OrderTimelineItemResponse> events
) {
}