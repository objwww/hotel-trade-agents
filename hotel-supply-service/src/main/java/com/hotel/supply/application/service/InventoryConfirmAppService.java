package com.hotel.supply.application.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.supply.infrastructure.persistence.mapper.DailyInventoryMapper;
import com.hotel.supply.infrastructure.persistence.mapper.InventoryFreezeRecordMapper;
import com.hotel.supply.infrastructure.persistence.po.InventoryFreezeRecordPO;
import com.hotel.supply.interfaces.request.InventoryConfirmRequest;
import com.hotel.supply.interfaces.response.InventoryConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InventoryConfirmAppService {

    private final DailyInventoryMapper dailyInventoryMapper;

    private final InventoryFreezeRecordMapper inventoryFreezeRecordMapper;

    @Transactional
    public InventoryConfirmResponse confirm(InventoryConfirmRequest request) {
        validate(request);

        InventoryFreezeRecordPO record = inventoryFreezeRecordMapper.selectOne(
                Wrappers.<InventoryFreezeRecordPO>lambdaQuery()
                        .eq(InventoryFreezeRecordPO::getFreezeToken, request.freezeToken())
                        .eq(InventoryFreezeRecordPO::getOrderNo, request.orderNo())
        );

        if (record == null) {
            throw new BizException(ErrorCode.INVENTORY_FREEZE_FAILED, "库存冻结记录不存在");
        }

        if ("CONFIRMED".equals(record.getFreezeStatus())) {
            return toResponse(record);
        }

        if (!"FROZEN".equals(record.getFreezeStatus())) {
            throw new BizException(
                    ErrorCode.INVENTORY_FREEZE_FAILED,
                    "当前冻结状态不可确认：" + record.getFreezeStatus()
            );
        }

        LocalDate current = record.getCheckInDate();

        while (current.isBefore(record.getCheckOutDate())) {
            int updated = dailyInventoryMapper.confirmInventory(
                    record.getRatePlanId(),
                    current,
                    record.getRoomCount()
            );

            if (updated != 1) {
                throw new BizException(
                        ErrorCode.INVENTORY_FREEZE_FAILED,
                        "日期 " + current + " 确认库存失败"
                );
            }

            current = current.plusDays(1);
        }

        record.setFreezeStatus("CONFIRMED");
        inventoryFreezeRecordMapper.updateById(record);

        return toResponse(record);
    }

    private void validate(InventoryConfirmRequest request) {
        if (!StringUtils.hasText(request.freezeToken())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "freezeToken 不能为空");
        }

        if (!StringUtils.hasText(request.orderNo())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "orderNo 不能为空");
        }
    }

    private InventoryConfirmResponse toResponse(InventoryFreezeRecordPO record) {
        return new InventoryConfirmResponse(
                record.getFreezeToken(),
                record.getOrderNo(),
                record.getRatePlanId(),
                record.getFreezeStatus(),
                record.getRoomCount()
        );
    }
}