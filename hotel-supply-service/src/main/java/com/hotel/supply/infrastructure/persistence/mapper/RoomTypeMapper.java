package com.hotel.supply.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.supply.infrastructure.persistence.po.RoomTypePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomTypeMapper extends BaseMapper<RoomTypePO> {
}