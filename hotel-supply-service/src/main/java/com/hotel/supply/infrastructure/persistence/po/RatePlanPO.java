package com.hotel.supply.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("rate_plan")
public class RatePlanPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String ratePlanId;

    private String hotelId;

    private String sellerId;

    private String roomTypeId;

    private String ratePlanName;

    private String breakfastType;

    private String cancelPolicyId;

    private String guestRequirementId;

    private String payType;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}