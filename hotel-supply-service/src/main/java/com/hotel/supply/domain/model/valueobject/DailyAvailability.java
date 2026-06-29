package com.hotel.supply.domain.model.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyAvailability {

    private LocalDate date;

    private BigDecimal price;

    private Integer availableCount;
}
