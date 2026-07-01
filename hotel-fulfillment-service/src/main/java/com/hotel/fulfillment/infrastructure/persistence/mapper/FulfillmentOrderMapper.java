package com.hotel.fulfillment.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.fulfillment.infrastructure.persistence.po.FulfillmentOrderPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FulfillmentOrderMapper extends BaseMapper<FulfillmentOrderPO> {
}