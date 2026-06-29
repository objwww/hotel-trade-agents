package com.hotel.supply.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.supply.domain.model.aggregate.RatePlan;
import com.hotel.supply.domain.repository.RatePlanRepository;
import com.hotel.supply.infrastructure.persistence.mapper.RatePlanMapper;
import com.hotel.supply.infrastructure.persistence.po.RatePlanPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RatePlanRepositoryImpl implements RatePlanRepository {

    private final RatePlanMapper ratePlanMapper;

    @Override
    public Optional<RatePlan> findByRatePlanId(String ratePlanId) {
        RatePlanPO po = ratePlanMapper.selectOne(
                Wrappers.<RatePlanPO>lambdaQuery()
                        .eq(RatePlanPO::getRatePlanId, ratePlanId)
                        .eq(RatePlanPO::getStatus, "ONLINE")
        );

        return Optional.ofNullable(po).map(this::toDomain);
    }

    @Override
    public List<RatePlan> findByHotelId(String hotelId) {
        List<RatePlanPO> poList = ratePlanMapper.selectList(
                Wrappers.<RatePlanPO>lambdaQuery()
                        .eq(RatePlanPO::getHotelId, hotelId)
                        .eq(RatePlanPO::getStatus, "ONLINE")
        );

        return poList.stream()
                .map(this::toDomain)
                .toList();
    }

    private RatePlan toDomain(RatePlanPO po) {
        return new RatePlan(
                po.getRatePlanId(),
                po.getHotelId(),
                po.getSellerId(),
                po.getRoomTypeId(),
                po.getRatePlanName(),
                po.getBreakfastType(),
                po.getStatus()
        );
    }
}