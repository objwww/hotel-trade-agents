package com.hotel.admin.application.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.admin.infrastructure.persistence.mapper.AdminOrderStatusLogMapper;
import com.hotel.admin.infrastructure.persistence.mapper.AdminSalesOrderMapper;
import com.hotel.admin.infrastructure.persistence.mapper.AdminTradeOperationLogMapper;
import com.hotel.admin.infrastructure.persistence.mapper.PerformanceMetricDTO;
import com.hotel.admin.infrastructure.persistence.po.AdminOrderStatusLogPO;
import com.hotel.admin.infrastructure.persistence.po.AdminSalesOrderPO;
import com.hotel.admin.infrastructure.persistence.po.AdminTradeOperationLogPO;
import com.hotel.admin.interfaces.response.MonitorOrderItemResponse;
import com.hotel.admin.interfaces.response.MonitorOverviewResponse;
import com.hotel.admin.interfaces.response.MonitorTraceResponse;
import com.hotel.admin.interfaces.response.PerformanceMetricResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMonitorAppService {

    private final AdminSalesOrderMapper salesOrderMapper;

    private final AdminOrderStatusLogMapper orderStatusLogMapper;

    private final AdminTradeOperationLogMapper tradeOperationLogMapper;

    public MonitorOverviewResponse overview() {
        Long totalOrderCount = salesOrderMapper.selectCount(null);

        Long pendingPaymentCount = countByStatus("PENDING_PAYMENT");
        Long paidCount = countByStatus("PAID");
        Long confirmedCount = countByStatus("CONFIRMED");
        Long cancelledCount = countByStatus("CANCELLED");

        Long successOrderCount = paidCount + confirmedCount;
        Long failedOrderCount = cancelledCount;

        Long apiSuccessCount = tradeOperationLogMapper.selectCount(
                Wrappers.<AdminTradeOperationLogPO>lambdaQuery()
                        .eq(AdminTradeOperationLogPO::getSuccess, true)
        );

        Long apiFailureCount = tradeOperationLogMapper.selectCount(
                Wrappers.<AdminTradeOperationLogPO>lambdaQuery()
                        .eq(AdminTradeOperationLogPO::getSuccess, false)
        );

        return new MonitorOverviewResponse(
                totalOrderCount,
                successOrderCount,
                failedOrderCount,
                pendingPaymentCount,
                paidCount,
                confirmedCount,
                cancelledCount,
                salesOrderMapper.sumSuccessAmount(),
                apiSuccessCount,
                apiFailureCount,
                tradeOperationLogMapper.avgCostMs()
        );
    }

    public List<MonitorOrderItemResponse> successOrders() {
        List<AdminSalesOrderPO> orders = salesOrderMapper.selectList(
                Wrappers.<AdminSalesOrderPO>lambdaQuery()
                        .in(AdminSalesOrderPO::getOrderStatus, List.of("PAID", "CONFIRMED"))
                        .orderByDesc(AdminSalesOrderPO::getId)
        );

        return orders.stream()
                .map(order -> toOrderItem(order, null))
                .toList();
    }

    public List<MonitorOrderItemResponse> failedOrders() {
        List<AdminSalesOrderPO> orders = salesOrderMapper.selectList(
                Wrappers.<AdminSalesOrderPO>lambdaQuery()
                        .eq(AdminSalesOrderPO::getOrderStatus, "CANCELLED")
                        .orderByDesc(AdminSalesOrderPO::getId)
        );

        return orders.stream()
                .map(order -> {
                    String failedTraceId = findFailureTraceId(order.getOrderNo());
                    return toOrderItem(order, failedTraceId);
                })
                .toList();
    }

    public AdminSalesOrderPO orderDetail(String orderNo) {
        return salesOrderMapper.selectOne(
                Wrappers.<AdminSalesOrderPO>lambdaQuery()
                        .eq(AdminSalesOrderPO::getOrderNo, orderNo)
        );
    }

    public List<AdminOrderStatusLogPO> orderTimeline(String orderNo) {
        return orderStatusLogMapper.selectList(
                Wrappers.<AdminOrderStatusLogPO>lambdaQuery()
                        .eq(AdminOrderStatusLogPO::getOrderNo, orderNo)
                        .orderByAsc(AdminOrderStatusLogPO::getCreatedAt)
                        .orderByAsc(AdminOrderStatusLogPO::getId)
        );
    }

    public MonitorTraceResponse trace(String traceId) {
        List<AdminTradeOperationLogPO> operationLogs = tradeOperationLogMapper.selectList(
                Wrappers.<AdminTradeOperationLogPO>lambdaQuery()
                        .eq(AdminTradeOperationLogPO::getTraceId, traceId)
                        .orderByAsc(AdminTradeOperationLogPO::getCreatedAt)
        );

        List<AdminOrderStatusLogPO> statusLogs = orderStatusLogMapper.selectList(
                Wrappers.<AdminOrderStatusLogPO>lambdaQuery()
                        .eq(AdminOrderStatusLogPO::getSourceTraceId, traceId)
                        .orderByAsc(AdminOrderStatusLogPO::getCreatedAt)
        );

        return new MonitorTraceResponse(traceId, operationLogs, statusLogs);
    }

    public List<PerformanceMetricResponse> performance() {
        List<PerformanceMetricDTO> metrics = tradeOperationLogMapper.queryPerformanceMetrics();

        return metrics.stream()
                .map(item -> new PerformanceMetricResponse(
                        item.getOperationType(),
                        item.getTotalCount(),
                        item.getSuccessCount(),
                        item.getFailureCount(),
                        item.getAvgCostMs(),
                        item.getMaxCostMs()
                ))
                .toList();
    }

    private Long countByStatus(String status) {
        return salesOrderMapper.selectCount(
                Wrappers.<AdminSalesOrderPO>lambdaQuery()
                        .eq(AdminSalesOrderPO::getOrderStatus, status)
        );
    }

    private String findFailureTraceId(String orderNo) {
        AdminOrderStatusLogPO log = orderStatusLogMapper.selectOne(
                Wrappers.<AdminOrderStatusLogPO>lambdaQuery()
                        .eq(AdminOrderStatusLogPO::getOrderNo, orderNo)
                        .eq(AdminOrderStatusLogPO::getEventType, "ORDER_CANCELLED")
                        .orderByDesc(AdminOrderStatusLogPO::getId)
                        .last("LIMIT 1")
        );

        if (log == null) {
            return null;
        }

        return log.getSourceTraceId();
    }

    private MonitorOrderItemResponse toOrderItem(
            AdminSalesOrderPO order,
            String failedTraceId
    ) {
        return new MonitorOrderItemResponse(
                order.getOrderNo(),
                order.getClientOrderNo(),
                order.getOrderStatus(),
                order.getOrderAmount(),
                order.getCurrency(),
                order.getSourceTraceId(),
                failedTraceId,
                order.getCancelReason(),
                order.getCancelAt(),
                order.getCreatedAt()
        );
    }
}