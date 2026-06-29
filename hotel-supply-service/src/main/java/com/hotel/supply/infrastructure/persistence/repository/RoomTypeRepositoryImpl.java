package com.hotel.supply.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.supply.domain.model.entity.RoomType;
import com.hotel.supply.domain.repository.RoomTypeRepository;
import com.hotel.supply.infrastructure.persistence.mapper.RoomTypeMapper;
import com.hotel.supply.infrastructure.persistence.po.RoomTypePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomTypeRepositoryImpl implements RoomTypeRepository {

    private final RoomTypeMapper roomTypeMapper;

    @Override
    public List<RoomType> findByHotelId(String hotelId) {
        List<RoomTypePO> poList = roomTypeMapper.selectList(
                Wrappers.<RoomTypePO>lambdaQuery()
                        .eq(RoomTypePO::getHotelId, hotelId)
                        .eq(RoomTypePO::getStatus, "ONLINE")
        );

        return poList.stream()
                .map(this::toDomain)
                .toList();
    }

    private RoomType toDomain(RoomTypePO po) {
        return new RoomType(
                po.getRoomTypeId(),
                po.getHotelId(),
                po.getRoomTypeName(),
                po.getBedType(),
                po.getMaxGuests(),
                po.getArea(),
                po.getStatus()
        );
    }
}