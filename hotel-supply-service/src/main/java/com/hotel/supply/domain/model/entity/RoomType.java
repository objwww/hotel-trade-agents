package com.hotel.supply.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomType {

    private String roomTypeId;

    private String hotelId;

    private String roomTypeName;

    private String bedType;

    private Integer maxGuests;

    private String area;

    private String status;
}