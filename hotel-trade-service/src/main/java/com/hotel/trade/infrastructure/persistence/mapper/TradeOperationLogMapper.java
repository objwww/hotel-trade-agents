package com.hotel.trade.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.trade.infrastructure.persistence.po.TradeOperationLogPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradeOperationLogMapper extends BaseMapper<TradeOperationLogPO> {
}