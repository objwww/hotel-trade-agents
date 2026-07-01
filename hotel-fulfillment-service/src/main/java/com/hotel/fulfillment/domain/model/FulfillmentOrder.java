package com.hotel.fulfillment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class FulfillmentOrder {

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
}