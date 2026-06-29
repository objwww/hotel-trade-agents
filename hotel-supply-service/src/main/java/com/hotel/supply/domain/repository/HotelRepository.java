package com.hotel.supply.domain.repository;

import com.hotel.supply.domain.model.aggregate.Hotel;

import java.util.Optional;

public interface HotelRepository {

    Optional<Hotel> findByHotelId(String hotelId);
}