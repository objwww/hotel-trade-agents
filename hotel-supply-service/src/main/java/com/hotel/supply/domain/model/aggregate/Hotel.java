package com.hotel.supply.domain.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Hotel {

    private String hotelId;

    private String hotelName;

    private String country;

    private String city;

    private String address;

    private Integer starLevel;

    private String status;
}