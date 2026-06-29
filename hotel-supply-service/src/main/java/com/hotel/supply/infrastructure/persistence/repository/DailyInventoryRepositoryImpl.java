package com.hotel.supply.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.supply.domain.model.aggregate.DailyInventory;
import com.hotel.supply.domain.model.valueobject.DateRange;
import com.hotel.supply.domain.repository.DailyInventoryRepository;
import com.hotel.supply.infrastructure.persistence.mapper.DailyInventoryMapper;
import com.hotel.supply.infrastructure.persistence.po.DailyInventoryPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DailyInventoryRepositoryImpl implements DailyInventoryRepository {

    private final DailyInventoryMapper dailyInventoryMapper;

    @Override
    public List<DailyInventory> findByRatePlanIdAndDateRange(String ratePlanId, DateRange dateRange) {
        List<DailyInventoryPO> poList = dailyInventoryMapper.selectList(
                Wrappers.<DailyInventoryPO>lambdaQuery()
                        .eq(DailyInventoryPO::getRatePlanId, ratePlanId)
                        .ge(DailyInventoryPO::getBizDate, dateRange.getCheckInDate())
                        .lt(DailyInventoryPO::getBizDate, dateRange.getCheckOutDate())
        );

        return poList.stream()
                .map(this::toDomain)
                .toList();
    }

    private DailyInventory toDomain(DailyInventoryPO po) {
        return new DailyInventory(
                po.getRatePlanId(),
                po.getBizDate(),
                po.getTotalCount(),
                po.getFrozenCount(),
                po.getSoldCount(),
                po.getSafetyStock()
        );
    }
}