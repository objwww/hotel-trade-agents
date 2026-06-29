package com.hotel.supply.domain.repository;

import com.hotel.supply.domain.model.aggregate.DailyInventory;
import com.hotel.supply.domain.model.valueobject.DateRange;

import java.util.List;

public interface DailyInventoryRepository {

    List<DailyInventory> findByRatePlanIdAndDateRange(String ratePlanId, DateRange dateRange);
}