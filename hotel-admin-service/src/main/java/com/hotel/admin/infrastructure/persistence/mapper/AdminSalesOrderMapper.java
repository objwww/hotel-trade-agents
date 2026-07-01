package com.hotel.admin.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.admin.infrastructure.persistence.po.AdminSalesOrderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface AdminSalesOrderMapper extends BaseMapper<AdminSalesOrderPO> {

    @Select("""
            SELECT COALESCE(SUM(order_amount), 0)
            FROM sales_order
            WHERE order_status IN ('PAID', 'CONFIRMED')
            """)
    BigDecimal sumSuccessAmount();
}