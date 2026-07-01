package com.hotel.guide.application.service;

import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.guide.application.assembler.GuideAssembler;
import com.hotel.guide.application.query.GuideHotelDetailQuery;
import com.hotel.guide.domain.model.GuideOffer;
import com.hotel.guide.domain.repository.GuideOfferRepository;
import com.hotel.guide.infrastructure.client.supply.SupplyClient;
import com.hotel.guide.infrastructure.client.supply.response.SupplyAvailabilityResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyHotelResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyRatePlanResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyRoomTypeResponse;
import com.hotel.guide.interfaces.response.GuideHotelDetailResponse;
import com.hotel.guide.interfaces.response.GuideHotelDetailSaleItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideHotelDetailAppService {

    private final GuideOfferRepository guideOfferRepository;

    private final SupplyClient supplyClient;

    private final GuideAssembler guideAssembler;

    public GuideHotelDetailResponse detail(GuideHotelDetailQuery query) {
        validate(query);

        SupplyHotelResponse hotel = supplyClient.getHotel(query.hotelId());

        List<GuideOffer> offers = guideOfferRepository.findByHotelIdAndDateRange(
                query.hotelId(),
                query.checkInDate(),
                query.checkOutDate()
        );

        List<SupplyRoomTypeResponse> roomTypes = supplyClient.listRooms(query.hotelId());
        List<SupplyRatePlanResponse> ratePlans = supplyClient.listRatePlans(query.hotelId());

        List<GuideHotelDetailSaleItemResponse> saleItems = new ArrayList<>();

        for (GuideOffer offer : offers) {
            SupplyRoomTypeResponse roomType = roomTypes.stream()
                    .filter(item -> offer.getRoomTypeId().equals(item.roomTypeId()))
                    .findFirst()
                    .orElseThrow(() -> new BizException(ErrorCode.SYSTEM_ERROR, "详情页房型不存在"));

            SupplyRatePlanResponse ratePlan = ratePlans.stream()
                    .filter(item -> offer.getRatePlanId().equals(item.ratePlanId()))
                    .findFirst()
                    .orElseThrow(() -> new BizException(ErrorCode.SYSTEM_ERROR, "详情页售卖计划不存在"));

            SupplyAvailabilityResponse availability = supplyClient.queryAvailability(
                    offer.getRatePlanId(),
                    query.checkInDate(),
                    query.checkOutDate(),
                    query.roomCount()
            );

            if (!Boolean.TRUE.equals(availability.available())) {
                continue;
            }

            GuideHotelDetailSaleItemResponse saleItem = guideAssembler.toDetailSaleItem(
                    offer,
                    roomType,
                    ratePlan,
                    availability
            );

            saleItems.add(saleItem);
        }

        return guideAssembler.toHotelDetail(
                hotel,
                query.checkInDate(),
                query.checkOutDate(),
                query.roomCount(),
                saleItems
        );
    }

    private void validate(GuideHotelDetailQuery query) {
        if (!StringUtils.hasText(query.hotelId())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "hotelId 不能为空");
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