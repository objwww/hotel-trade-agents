package com.hotel.supply.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("daily_rate")
public class DailyRatePO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String ratePlanId;

    private LocalDate bizDate;

    private BigDecimal salePrice;

    private String currency;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}