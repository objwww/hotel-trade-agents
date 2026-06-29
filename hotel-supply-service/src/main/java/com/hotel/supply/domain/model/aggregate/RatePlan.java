package com.hotel.supply.domain.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatePlan {

    private String ratePlanId;

    private String hotelId;

    private String sellerId;

    private String roomTypeId;

    private String ratePlanName;

    private String breakfastType;

    private String status;
}