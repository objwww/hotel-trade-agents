package com.hotel.supply.domain.service;

import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.supply.domain.model.aggregate.DailyInventory;
import com.hotel.supply.domain.model.aggregate.RatePlan;
import com.hotel.supply.domain.model.entity.DailyRate;
import com.hotel.supply.domain.model.valueobject.Availability;
import com.hotel.supply.domain.model.valueobject.DailyAvailability;
import com.hotel.supply.domain.model.valueobject.DateRange;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AvailabilityDomainService {

    public Availability calculate(
            RatePlan ratePlan,
            DateRange dateRange,
            List<DailyRate> dailyRates,
            List<DailyInventory> dailyInventories,
            Integer roomCount
    ) {
        if (roomCount == null || roomCount <= 0) {
            throw new BizException(ErrorCode.PARAM_INVALID, "roomCount 必须大于 0");
        }

        Map<LocalDate, DailyRate> rateMap = dailyRates.stream()
                .collect(Collectors.toMap(DailyRate::getBizDate, Function.identity()));

        Map<LocalDate, DailyInventory> inventoryMap = dailyInventories.stream()
                .collect(Collectors.toMap(DailyInventory::getBizDate, Function.identity()));

        BigDecimal totalAmount = BigDecimal.ZERO;
        int maxBookableRoomCount = Integer.MAX_VALUE;
        String currency = "CNY";

        List<DailyAvailability> dailyAvailabilityList = new ArrayList<>();

        for (LocalDate date : dateRange.stayDates()) {
            DailyRate dailyRate = rateMap.get(date);
            if (dailyRate == null) {
                throw new BizException(ErrorCode.PRICE_NOT_FOUND, "日期 " + date + " 缺少价格");
            }

            currency = dailyRate.getCurrency();
            totalAmount = totalAmount.add(dailyRate.getSalePrice());

            DailyInventory dailyInventory = inventoryMap.get(date);
            int availableCount = dailyInventory == null ? 0 : dailyInventory.availableCount();

            maxBookableRoomCount = Math.min(maxBookableRoomCount, availableCount);

            dailyAvailabilityList.add(new DailyAvailability(
                    date,
                    dailyRate.getSalePrice(),
                    availableCount
            ));
        }

        BigDecimal finalAmount = totalAmount.multiply(BigDecimal.valueOf(roomCount));
        boolean available = maxBookableRoomCount >= roomCount;

        return new Availability(
                ratePlan.getRatePlanId(),
                dateRange.getCheckInDate(),
                dateRange.getCheckOutDate(),
                roomCount,
                available,
                maxBookableRoomCount,
                finalAmount,
                currency,
                dailyAvailabilityList
        );
    }
}