package com.hotel.guide.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.guide.domain.model.GuideOffer;
import com.hotel.guide.domain.repository.GuideOfferRepository;
import com.hotel.guide.infrastructure.persistence.mapper.GuideOfferMapper;
import com.hotel.guide.infrastructure.persistence.po.GuideOfferPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GuideOfferRepositoryImpl implements GuideOfferRepository {

    private final GuideOfferMapper guideOfferMapper;

    @Override
    public List<GuideOffer> findCandidates(LocalDate checkInDate, LocalDate checkOutDate) {
        List<GuideOfferPO> poList = guideOfferMapper.selectList(
                Wrappers.<GuideOfferPO>lambdaQuery()
                        .eq(GuideOfferPO::getCheckInDate, checkInDate)
                        .eq(GuideOfferPO::getCheckOutDate, checkOutDate)
        );

        return poList.stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<GuideOffer> findByHotelIdAndDateRange(
            String hotelId,
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {
        List<GuideOfferPO> poList = guideOfferMapper.selectList(
                Wrappers.<GuideOfferPO>lambdaQuery()
                        .eq(GuideOfferPO::getHotelId, hotelId)
                        .eq(GuideOfferPO::getCheckInDate, checkInDate)
                        .eq(GuideOfferPO::getCheckOutDate, checkOutDate)
        );

        return poList.stream()
                .map(this::toDomain)
                .toList();
    }

    private GuideOffer toDomain(GuideOfferPO po) {
        return new GuideOffer(
                po.getGuideOfferId(),
                po.getHotelId(),
                po.getSellerId(),
                po.getRoomTypeId(),
                po.getRatePlanId(),
                po.getCheckInDate(),
                po.getCheckOutDate(),
                po.getRoomCount(),
                po.getDisplayAmount(),
                po.getCurrency(),
                po.getInventoryTag(),
                po.getBreakfastTag(),
                po.getCancelTag(),
                po.getDataFreshness()
        );
    }
}