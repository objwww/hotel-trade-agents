package com.hotel.supply.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("inventory_freeze_record")
public class InventoryFreezeRecordPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String freezeToken;

    private String orderNo;

    private String ratePlanId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer roomCount;

    private String freezeStatus;

    private String sourceTraceId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}