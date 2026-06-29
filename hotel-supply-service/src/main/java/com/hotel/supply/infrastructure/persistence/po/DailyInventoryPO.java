package com.hotel.supply.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("daily_inventory")
public class DailyInventoryPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String ratePlanId;

    private LocalDate bizDate;

    private Integer totalCount;

    private Integer frozenCount;

    private Integer soldCount;

    private Integer safetyStock;

    private Integer version;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}