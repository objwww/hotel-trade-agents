package com.hotel.trade.interfaces.response;

import java.time.LocalDateTime;

public record OrderTimelineItemResponse(
        String eventType,
        String fromStatus,
        String toStatus,
        String eventDesc,
        String sourceTraceId,
        LocalDateTime createdAt
) {
}