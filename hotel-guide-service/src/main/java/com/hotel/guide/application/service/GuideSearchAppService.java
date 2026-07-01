package com.hotel.guide.application.service;

import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.guide.application.assembler.GuideAssembler;
import com.hotel.guide.application.query.GuideSearchQuery;
import com.hotel.guide.domain.model.GuideOffer;
import com.hotel.guide.domain.repository.GuideOfferRepository;
import com.hotel.guide.infrastructure.client.supply.SupplyClient;
import com.hotel.guide.infrastructure.client.supply.response.SupplyAvailabilityResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyHotelResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyRatePlanResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyRoomTypeResponse;
import com.hotel.guide.interfaces.response.GuideHotelSearchItemResponse;
import com.hotel.guide.interfaces.response.GuideSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideSearchAppService {

    private final GuideOfferRepository guideOfferRepository;

    private final SupplyClient supplyClient;

    private final GuideAssembler guideAssembler;

    public GuideSearchResponse search(GuideSearchQuery query) {
        validate(query);

        List<GuideOffer> candidates = guideOfferRepository.findCandidates(
                query.checkInDate(),
                query.checkOutDate()
        );

        List<GuideHotelSearchItemResponse> items = new ArrayList<>();

        for (GuideOffer offer : candidates) {
            SupplyHotelResponse hotel = supplyClient.getHotel(offer.getHotelId());

            if (StringUtils.hasText(query.city()) && !query.city().equals(hotel.city())) {
                continue;
            }

            SupplyAvailabilityResponse availability = supplyClient.queryAvailability(
                    offer.getRatePlanId(),
                    query.checkInDate(),
                    query.checkOutDate(),
                    query.roomCount()
            );

            if (!Boolean.TRUE.equals(availability.available())) {
                continue;
            }

            List<SupplyRoomTypeResponse> roomTypes = supplyClient.listRooms(offer.getHotelId());
            SupplyRoomTypeResponse roomType = roomTypes.stream()
                    .filter(item -> offer.getRoomTypeId().equals(item.roomTypeId()))
                    .findFirst()
                    .orElseThrow(() -> new BizException(ErrorCode.SYSTEM_ERROR, "导购房型不存在"));

            List<SupplyRatePlanResponse> ratePlans = supplyClient.listRatePlans(offer.getHotelId());
            SupplyRatePlanResponse ratePlan = ratePlans.stream()
                    .filter(item -> offer.getRatePlanId().equals(item.ratePlanId()))
                    .findFirst()
                    .orElseThrow(() -> new BizException(ErrorCode.SYSTEM_ERROR, "导购售卖计划不存在"));

            GuideHotelSearchItemResponse item = guideAssembler.toSearchItem(
                    offer,
                    hotel,
                    roomType,
                    ratePlan,
                    availability
            );

            items.add(item);
        }

        return new GuideSearchResponse(items.size(), items);
    }

    private void validate(GuideSearchQuery query) {
        if (!StringUtils.hasText(query.city())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "city 不能为空");
        }

        if (query.checkInDate() == null || query.checkOutDate() == null) {
            throw new BizException(ErrorCode.DATE_RANGE_INVALID);
        }

        if (!query.checkInDate().isBefore(query.checkOutDate())) {
            throw new BizException(ErrorCode.DATE_RANGE_INVALID);
        }

        if (query.roomCount() == null || query.roomCount() <= 0) {
            throw new BizException(ErrorCode.PARAM_INVALID, "roomCount 必须大于 0");
        }
    }
}