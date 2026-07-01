package com.hotel.fulfillment.application.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.common.trace.TraceContext;
import com.hotel.fulfillment.infrastructure.persistence.mapper.FulfillmentOrderMapper;
import com.hotel.fulfillment.infrastructure.persistence.po.FulfillmentOrderPO;
import com.hotel.fulfillment.interfaces.request.FulfillmentConfirmRequest;
import com.hotel.fulfillment.interfaces.response.FulfillmentConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FulfillmentConfirmAppService {

    private final FulfillmentOrderMapper fulfillmentOrderMapper;

    public FulfillmentConfirmResponse confirm(FulfillmentConfirmRequest request) {
        validate(request);

        FulfillmentOrderPO existed = fulfillmentOrderMapper.selectOne(
                Wrappers.<FulfillmentOrderPO>lambdaQuery()
                        .eq(FulfillmentOrderPO::getOrderNo, request.orderNo())
        );

        if (existed != null) {
            return new FulfillmentConfirmResponse(
                    existed.getFulfillmentNo(),
                    existed.getOrderNo(),
                    existed.getFulfillmentStatus(),
                    existed.getSupplierConfirmNo()
            );
        }

        String fulfillmentNo = generateFulfillmentNo();
        String supplierConfirmNo = generateSupplierConfirmNo();

        FulfillmentOrderPO po = new FulfillmentOrderPO();
        po.setFulfillmentNo(fulfillmentNo);
        po.setOrderNo(request.orderNo());

        po.setHotelId(request.hotelId());
        po.setSellerId(request.sellerId());
        po.setRoomTypeId(request.roomTypeId());
        po.setRatePlanId(request.ratePlanId());

        po.setCheckInDate(request.checkInDate());
        po.setCheckOutDate(request.checkOutDate());
        po.setRoomCount(request.roomCount());

        po.setContactName(request.contactName());
        po.setContactPhone(request.contactPhone());

        po.setFulfillmentStatus("CONFIRMED");
        po.setSupplierConfirmNo(supplierConfirmNo);
        po.setSourceTraceId(TraceContext.getTraceId());

        po.setSupplierResponseJson("""
                {"mockSupplier":"HOTEL_SUPPLIER","result":"CONFIRMED"}
                """);

        fulfillmentOrderMapper.insert(po);

        return new FulfillmentConfirmResponse(
                fulfillmentNo,
                request.orderNo(),
                "CONFIRMED",
                supplierConfirmNo
        );
    }

    private void validate(FulfillmentConfirmRequest request) {
        if (!StringUtils.hasText(request.orderNo())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "orderNo 不能为空");
        }

        if (!StringUtils.hasText(request.hotelId())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "hotelId 不能为空");
        }

        if (!StringUtils.hasText(request.sellerId())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "sellerId 不能为空");
        }

        if (!StringUtils.hasText(request.roomTypeId())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "roomTypeId 不能为空");
        }

        if (!StringUtils.hasText(request.ratePlanId())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "ratePlanId 不能为空");
        }

        if (request.checkInDate() == null || request.checkOutDate() == null) {
            throw new BizException(ErrorCode.DATE_RANGE_INVALID);
        }

        if (!request.checkInDate().isBefore(request.checkOutDate())) {
            throw new BizException(ErrorCode.DATE_RANGE_INVALID);
        }

        if (request.roomCount() == null || request.roomCount() <= 0) {
            throw new BizException(ErrorCode.PARAM_INVALID, "roomCount 必须大于 0");
        }
    }

    private String generateFulfillmentNo() {
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "FO" + timePart + randomPart;
    }

    private String generateSupplierConfirmNo() {
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "SCN" + timePart + randomPart;
    }
}