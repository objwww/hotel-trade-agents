package com.hotel.admin.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("sales_order")
public class AdminSalesOrderPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private String quoteToken;

    private String clientOrderNo;

    private String hotelId;

    private String sellerId;

    private String roomTypeId;

    private String ratePlanId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer roomCount;

    private BigDecimal orderAmount;

    private String currency;

    private String orderStatus;

    private String paymentNo;

    private LocalDateTime paidAt;

    private String freezeToken;

    private String fulfillmentNo;

    private String supplierConfirmNo;

    private LocalDateTime confirmedAt;

    private String contactName;

    private String contactPhone;

    private String sourceTraceId;

    private String cancelReason;

    private LocalDateTime cancelAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}