package com.hotel.fulfillment.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("fulfillment_order")
public class FulfillmentOrderPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String fulfillmentNo;

    private String orderNo;

    private String hotelId;

    private String sellerId;

    private String roomTypeId;

    private String ratePlanId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer roomCount;

    private String contactName;

    private String contactPhone;

    private String fulfillmentStatus;

    private String supplierConfirmNo;

    private String sourceTraceId;

    private String supplierResponseJson;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}