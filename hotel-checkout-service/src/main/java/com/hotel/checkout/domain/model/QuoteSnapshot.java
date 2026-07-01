package com.hotel.checkout.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class QuoteSnapshot {

    private String quoteToken;

    private String hotelId;

    private String sellerId;

    private String roomTypeId;

    private String ratePlanId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer roomCount;

    private Boolean available;

    private Integer maxBookableRoomCount;

    private BigDecimal totalAmount;

    private String currency;

    private String quoteStatus;

    private LocalDateTime expireAt;

    private String sourceTraceId;

    private String dailySnapshotJson;
}