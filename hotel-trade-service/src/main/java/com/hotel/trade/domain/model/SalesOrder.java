package com.hotel.trade.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SalesOrder {

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

    private String freezeToken;

    private String contactName;

    private String contactPhone;

    private String sourceTraceId;

    private String cancelReason;

    private LocalDateTime cancelAt;
    private String paymentNo;

    private LocalDateTime paidAt;
    private String fulfillmentNo;

    private String supplierConfirmNo;

    private LocalDateTime confirmedAt;
}