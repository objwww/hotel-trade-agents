package com.hotel.trade.application.assembler;

import com.hotel.trade.infrastructure.persistence.po.OrderStatusLogPO;
import com.hotel.trade.interfaces.response.OrderDetailResponse;
import com.hotel.trade.interfaces.response.OrderTimelineItemResponse;
import com.hotel.trade.interfaces.response.OrderTimelineResponse;

import java.util.List;
import com.hotel.trade.domain.model.SalesOrder;
import com.hotel.trade.interfaces.response.OrderCancelResponse;
import com.hotel.trade.interfaces.response.OrderCreateResponse;
import com.hotel.trade.interfaces.response.OrderFulfillmentResponse;
import com.hotel.trade.interfaces.response.OrderPaySuccessResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderAssembler {

    public OrderCreateResponse toCreateResponse(SalesOrder order) {
        return new OrderCreateResponse(
                order.getOrderNo(),
                order.getQuoteToken(),
                order.getClientOrderNo(),

                order.getHotelId(),
                order.getSellerId(),
                order.getRoomTypeId(),
                order.getRatePlanId(),

                order.getCheckInDate(),
                order.getCheckOutDate(),
                order.getRoomCount(),

                order.getOrderAmount(),
                order.getCurrency(),
                order.getOrderStatus(),
                order.getFreezeToken()
        );
    }

    public OrderCancelResponse toCancelResponse(
            String orderNo,
            String orderStatus,
            String freezeToken,
            String cancelReason,
            LocalDateTime cancelAt
    ) {
        return new OrderCancelResponse(
                orderNo,
                orderStatus,
                freezeToken,
                cancelReason,
                cancelAt
        );
    }
    public OrderPaySuccessResponse toPaySuccessResponse(
            String orderNo,
            String orderStatus,
            String freezeToken,
            String paymentNo,
            LocalDateTime paidAt
    ) {
        return new OrderPaySuccessResponse(
                orderNo,
                orderStatus,
                freezeToken,
                paymentNo,
                paidAt
        );
    }
    public OrderFulfillmentResponse toFulfillmentResponse(
            String orderNo,
            String orderStatus,
            String fulfillmentNo,
            String supplierConfirmNo,
            LocalDateTime confirmedAt
    ) {
        return new OrderFulfillmentResponse(
                orderNo,
                orderStatus,
                fulfillmentNo,
                supplierConfirmNo,
                confirmedAt
        );
    }

    public OrderDetailResponse toDetailResponse(SalesOrder order) {
        return new OrderDetailResponse(
                order.getOrderNo(),
                order.getQuoteToken(),
                order.getClientOrderNo(),

                order.getHotelId(),
                order.getSellerId(),
                order.getRoomTypeId(),
                order.getRatePlanId(),

                order.getCheckInDate(),
                order.getCheckOutDate(),
                order.getRoomCount(),

                order.getOrderAmount(),
                order.getCurrency(),

                order.getOrderStatus(),

                order.getPaymentNo(),
                order.getPaidAt(),

                order.getFreezeToken(),

                order.getFulfillmentNo(),
                order.getSupplierConfirmNo(),
                order.getConfirmedAt(),

                order.getContactName(),
                order.getContactPhone(),

                order.getCancelReason(),
                order.getCancelAt(),

                order.getSourceTraceId()
        );
    }

    public OrderTimelineResponse toTimelineResponse(
            String orderNo,
            List<OrderStatusLogPO> logs
    ) {
        List<OrderTimelineItemResponse> events = logs.stream()
                .map(item -> new OrderTimelineItemResponse(
                        item.getEventType(),
                        item.getFromStatus(),
                        item.getToStatus(),
                        item.getEventDesc(),
                        item.getSourceTraceId(),
                        item.getCreatedAt()
                ))
                .toList();

        return new OrderTimelineResponse(
                orderNo,
                events.size(),
                events
        );
    }
}