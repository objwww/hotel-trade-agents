package com.hotel.supply.domain.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyInventory {

    private String ratePlanId;

    private LocalDate bizDate;

    private Integer totalCount;

    private Integer frozenCount;

    private Integer soldCount;

    private Integer safetyStock;

    public int availableCount() {
        return Math.max(totalCount - frozenCount - soldCount - safetyStock, 0);
    }
}