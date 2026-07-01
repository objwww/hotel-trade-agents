package com.hotel.guide.interfaces.response;

import java.util.List;

public record GuideSearchResponse(
        Integer total,
        List<GuideHotelSearchItemResponse> hotels
) {
}