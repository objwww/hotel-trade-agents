package com.hotel.supply.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyRate {

    private String ratePlanId;

    private LocalDate bizDate;

    private BigDecimal salePrice;

    private String currency;
}