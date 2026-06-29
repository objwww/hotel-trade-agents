package com.hotel.supply.domain.repository;

import com.hotel.supply.domain.model.entity.DailyRate;
import com.hotel.supply.domain.model.valueobject.DateRange;

import java.util.List;

public interface DailyRateRepository {

    List<DailyRate> findByRatePlanIdAndDateRange(String ratePlanId, DateRange dateRange);
}
