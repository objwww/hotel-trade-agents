package com.hotel.supply.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.supply.domain.model.entity.DailyRate;
import com.hotel.supply.domain.model.valueobject.DateRange;
import com.hotel.supply.domain.repository.DailyRateRepository;
import com.hotel.supply.infrastructure.persistence.mapper.DailyRateMapper;
import com.hotel.supply.infrastructure.persistence.po.DailyRatePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DailyRateRepositoryImpl implements DailyRateRepository {

    private final DailyRateMapper dailyRateMapper;

    @Override
    public List<DailyRate> findByRatePlanIdAndDateRange(String ratePlanId, DateRange dateRange) {
        List<DailyRatePO> poList = dailyRateMapper.selectList(
                Wrappers.<DailyRatePO>lambdaQuery()
                        .eq(DailyRatePO::getRatePlanId, ratePlanId)
                        .ge(DailyRatePO::getBizDate, dateRange.getCheckInDate())
                        .lt(DailyRatePO::getBizDate, dateRange.getCheckOutDate())
                        .eq(DailyRatePO::getStatus, "ONLINE")
        );

        return poList.stream()
                .map(this::toDomain)
                .toList();
    }

    private DailyRate toDomain(DailyRatePO po) {
        return new DailyRate(
                po.getRatePlanId(),
                po.getBizDate(),
                po.getSalePrice(),
                po.getCurrency()
        );
    }
}