package com.hotel.guide.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.guide.infrastructure.persistence.po.GuideOfferPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GuideOfferMapper extends BaseMapper<GuideOfferPO> {
}