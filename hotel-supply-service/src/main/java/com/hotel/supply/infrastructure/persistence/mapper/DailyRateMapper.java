package com.hotel.supply.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.supply.infrastructure.persistence.po.DailyRatePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DailyRateMapper extends BaseMapper<DailyRatePO> {
}