package com.hotel.admin.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.admin.infrastructure.persistence.po.AdminTradeOperationLogPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminTradeOperationLogMapper extends BaseMapper<AdminTradeOperationLogPO> {

    @Select("""
            SELECT
                operation_type AS operationType,
                COUNT(*) AS totalCount,
                SUM(CASE WHEN success = 1 THEN 1 ELSE 0 END) AS successCount,
                SUM(CASE WHEN success = 0 THEN 1 ELSE 0 END) AS failureCount,
                AVG(cost_ms) AS avgCostMs,
                MAX(cost_ms) AS maxCostMs
            FROM trade_operation_log
            GROUP BY operation_type
            ORDER BY operation_type
            """)
    List<PerformanceMetricDTO> queryPerformanceMetrics();

    @Select("""
            SELECT COALESCE(AVG(cost_ms), 0)
            FROM trade_operation_log
            """)
    Double avgCostMs();
}