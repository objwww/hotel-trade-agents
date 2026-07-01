package com.hotel.admin.interfaces.controller;

import com.hotel.admin.application.service.AdminMonitorAppService;
import com.hotel.admin.infrastructure.persistence.po.AdminOrderStatusLogPO;
import com.hotel.admin.infrastructure.persistence.po.AdminSalesOrderPO;
import com.hotel.admin.interfaces.response.MonitorOrderItemResponse;
import com.hotel.admin.interfaces.response.MonitorOverviewResponse;
import com.hotel.admin.interfaces.response.MonitorTraceResponse;
import com.hotel.admin.interfaces.response.PerformanceMetricResponse;
import com.hotel.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/monitor")
public class AdminMonitorController {

    private final AdminMonitorAppService adminMonitorAppService;

    @GetMapping("/overview")
    public ApiResponse<MonitorOverviewResponse> overview() {
        return ApiResponse.success(adminMonitorAppService.overview());
    }

    @GetMapping("/orders/success")
    public ApiResponse<List<MonitorOrderItemResponse>> successOrders() {
        return ApiResponse.success(adminMonitorAppService.successOrders());
    }

    @GetMapping("/orders/failed")
    public ApiResponse<List<MonitorOrderItemResponse>> failedOrders() {
        return ApiResponse.success(adminMonitorAppService.failedOrders());
    }

    @GetMapping("/orders/{orderNo}")
    public ApiResponse<AdminSalesOrderPO> orderDetail(
            @PathVariable("orderNo") String orderNo
    ) {
        return ApiResponse.success(adminMonitorAppService.orderDetail(orderNo));
    }

    @GetMapping("/orders/{orderNo}/timeline")
    public ApiResponse<List<AdminOrderStatusLogPO>> orderTimeline(
            @PathVariable("orderNo") String orderNo
    ) {
        return ApiResponse.success(adminMonitorAppService.orderTimeline(orderNo));
    }

    @GetMapping("/traces/{traceId}")
    public ApiResponse<MonitorTraceResponse> trace(
            @PathVariable("traceId") String traceId
    ) {
        return ApiResponse.success(adminMonitorAppService.trace(traceId));
    }

    @GetMapping("/performance")
    public ApiResponse<List<PerformanceMetricResponse>> performance() {
        return ApiResponse.success(adminMonitorAppService.performance());
    }
}