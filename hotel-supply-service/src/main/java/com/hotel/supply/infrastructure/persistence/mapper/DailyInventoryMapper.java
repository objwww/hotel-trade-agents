package com.hotel.supply.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.supply.infrastructure.persistence.po.DailyInventoryPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DailyInventoryMapper extends BaseMapper<DailyInventoryPO> {
}