package com.hotel.trade.application.service;

import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.trade.application.assembler.OrderAssembler;
import com.hotel.trade.domain.model.SalesOrder;
import com.hotel.trade.domain.repository.SalesOrderRepository;
import com.hotel.trade.infrastructure.persistence.po.OrderStatusLogPO;
import com.hotel.trade.interfaces.response.OrderDetailResponse;
import com.hotel.trade.interfaces.response.OrderTimelineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryAppService {

    private final SalesOrderRepository salesOrderRepository;

    private final OrderStatusLogAppService orderStatusLogAppService;

    private final OrderAssembler orderAssembler;

    public OrderDetailResponse getOrderDetail(String orderNo) {
        if (!StringUtils.hasText(orderNo)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "orderNo 不能为空");
        }

        SalesOrder order = salesOrderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BizException(ErrorCode.ORDER_NOT_FOUND));

        return orderAssembler.toDetailResponse(order);
    }

    public OrderTimelineResponse getOrderTimeline(String orderNo) {
        if (!StringUtils.hasText(orderNo)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "orderNo 不能为空");
        }

        salesOrderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BizException(ErrorCode.ORDER_NOT_FOUND));

        List<OrderStatusLogPO> logs = orderStatusLogAppService.listByOrderNo(orderNo);

        return orderAssembler.toTimelineResponse(orderNo, logs);
    }
}