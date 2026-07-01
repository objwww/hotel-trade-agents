package com.hotel.guide.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("guide_offer")
public class GuideOfferPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String guideOfferId;

    private String hotelId;

    private String sellerId;

    private String roomTypeId;

    private String ratePlanId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer roomCount;

    private BigDecimal displayAmount;

    private String currency;

    private String inventoryTag;

    private String breakfastTag;

    private String cancelTag;

    private String dataFreshness;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}