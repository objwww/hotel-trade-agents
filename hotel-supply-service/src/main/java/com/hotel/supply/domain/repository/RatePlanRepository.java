package com.hotel.supply.domain.repository;

import com.hotel.supply.domain.model.aggregate.RatePlan;

import java.util.List;
import java.util.Optional;

public interface RatePlanRepository {

    Optional<RatePlan> findByRatePlanId(String ratePlanId);

    List<RatePlan> findByHotelId(String hotelId);
}