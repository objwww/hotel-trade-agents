package com.hotel.supply.domain.model.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class Availability {

    private String ratePlanId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer roomCount;

    private Boolean available;

    private Integer maxBookableRoomCount;

    private BigDecimal totalAmount;

    private String currency;

    private List<DailyAvailability> dailyAvailabilities;
}