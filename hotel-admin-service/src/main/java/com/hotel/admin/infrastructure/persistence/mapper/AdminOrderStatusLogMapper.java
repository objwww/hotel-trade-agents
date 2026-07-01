package com.hotel.admin.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.admin.infrastructure.persistence.po.AdminOrderStatusLogPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminOrderStatusLogMapper extends BaseMapper<AdminOrderStatusLogPO> {
}