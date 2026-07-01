package com.hotel.supply.application.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.common.trace.TraceContext;
import com.hotel.supply.infrastructure.persistence.mapper.DailyInventoryMapper;
import com.hotel.supply.infrastructure.persistence.mapper.InventoryFreezeRecordMapper;
import com.hotel.supply.infrastructure.persistence.po.InventoryFreezeRecordPO;
import com.hotel.supply.interfaces.request.InventoryFreezeRequest;
import com.hotel.supply.interfaces.response.InventoryFreezeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InventoryFreezeAppService {

    private final DailyInventoryMapper dailyInventoryMapper;

    private final InventoryFreezeRecordMapper inventoryFreezeRecordMapper;

    @Transactional
    public InventoryFreezeResponse freeze(InventoryFreezeRequest request) {
        validate(request);

        InventoryFreezeRecordPO existed = inventoryFreezeRecordMapper.selectOne(
                Wrappers.<InventoryFreezeRecordPO>lambdaQuery()
                        .eq(InventoryFreezeRecordPO::getFreezeToken, request.freezeToken())
        );

        if (existed != null) {
            return new InventoryFreezeResponse(
                    existed.getFreezeToken(),
                    existed.getOrderNo(),
                    existed.getRatePlanId(),
                    existed.getFreezeStatus(),
                    existed.getRoomCount()
            );
        }

        InventoryFreezeRecordPO record = new InventoryFreezeRecordPO();
        record.setFreezeToken(request.freezeToken());
        record.setOrderNo(request.orderNo());
        record.setRatePlanId(request.ratePlanId());
        record.setCheckInDate(request.checkInDate());
        record.setCheckOutDate(request.checkOutDate());
        record.setRoomCount(request.roomCount());
        record.setFreezeStatus("FREEZING");
        record.setSourceTraceId(TraceContext.getTraceId());

        inventoryFreezeRecordMapper.insert(record);

        LocalDate current = request.checkInDate();

        while (current.isBefore(request.checkOutDate())) {
            int updated = dailyInventoryMapper.freezeInventory(
                    request.ratePlanId(),
                    current,
                    request.roomCount()
            );

            if (updated != 1) {
                throw new BizException(
                        ErrorCode.INVENTORY_NOT_ENOUGH,
                        "日期 " + current + " 库存不足，冻结失败"
                );
            }

            current = current.plusDays(1);
        }

        record.setFreezeStatus("FROZEN");
        inventoryFreezeRecordMapper.updateById(record);

        return new InventoryFreezeResponse(
                request.freezeToken(),
                request.orderNo(),
                request.ratePlanId(),
                "FROZEN",
                request.roomCount()
        );
    }

    private void validate(InventoryFreezeRequest request) {
        if (!StringUtils.hasText(request.freezeToken())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "freezeToken 不能为空");
        }

        if (!StringUtils.hasText(request.orderNo())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "orderNo 不能为空");
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
}