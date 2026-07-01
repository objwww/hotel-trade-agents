package com.hotel.guide.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GuideOffer {

    private String guideOfferId;

    private String hotelId;

    private String sellerId;

    private String roomTypeId;

    private String ratePlanId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer roomCount;

    private BigDecimal displayAmount;

    private String currency;

    private String inventoryTag;

    private String breakfastTag;

    private String cancelTag;

    private String dataFreshness;
}