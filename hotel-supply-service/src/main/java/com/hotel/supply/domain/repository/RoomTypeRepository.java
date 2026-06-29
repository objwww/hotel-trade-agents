package com.hotel.supply.domain.repository;

import com.hotel.supply.domain.model.entity.RoomType;

import java.util.List;

public interface RoomTypeRepository {

    List<RoomType> findByHotelId(String hotelId);
}