package com.hotel.guide.domain.repository;

import com.hotel.guide.domain.model.GuideOffer;

import java.time.LocalDate;
import java.util.List;

public interface GuideOfferRepository {

    List<GuideOffer> findCandidates(LocalDate checkInDate, LocalDate checkOutDate);

    List<GuideOffer> findByHotelIdAndDateRange(
            String hotelId,
            LocalDate checkInDate,
            LocalDate checkOutDate
    );
}