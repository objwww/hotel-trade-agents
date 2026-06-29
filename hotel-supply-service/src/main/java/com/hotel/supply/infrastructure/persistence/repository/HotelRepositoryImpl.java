package com.hotel.supply.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.supply.domain.model.aggregate.Hotel;
import com.hotel.supply.domain.repository.HotelRepository;
import com.hotel.supply.infrastructure.persistence.mapper.HotelMapper;
import com.hotel.supply.infrastructure.persistence.po.HotelPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HotelRepositoryImpl implements HotelRepository {

    private final HotelMapper hotelMapper;

    @Override
    public Optional<Hotel> findByHotelId(String hotelId) {
        HotelPO po = hotelMapper.selectOne(
                Wrappers.<HotelPO>lambdaQuery()
                        .eq(HotelPO::getHotelId, hotelId)
                        .eq(HotelPO::getStatus, "ONLINE")
        );

        return Optional.ofNullable(po).map(this::toDomain);
    }

    private Hotel toDomain(HotelPO po) {
        return new Hotel(
                po.getHotelId(),
                po.getHotelName(),
                po.getCountry(),
                po.getCity(),
                po.getAddress(),
                po.getStarLevel(),
                po.getStatus()
        );
    }
}