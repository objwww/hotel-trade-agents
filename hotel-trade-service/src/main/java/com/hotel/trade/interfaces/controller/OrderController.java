package com.hotel.trade.interfaces.controller;

import com.hotel.trade.application.service.OrderQueryAppService;
import com.hotel.trade.application.service.TradeOperationLogService;
import com.hotel.trade.interfaces.response.OrderDetailResponse;
import com.hotel.trade.interfaces.response.OrderTimelineResponse;
import com.hotel.trade.application.command.OrderPaySuccessCommand;
import com.hotel.trade.interfaces.request.OrderPaySuccessRequest;
import com.hotel.trade.interfaces.response.OrderFulfillmentResponse;
import com.hotel.trade.interfaces.response.OrderPaySuccessResponse;
import com.hotel.common.api.ApiResponse;
import com.hotel.trade.application.command.OrderCreateCommand;
import com.hotel.trade.application.command.OrderPaySuccessCommand;
import com.hotel.trade.application.service.OrderAppService;
import com.hotel.trade.interfaces.request.OrderCreateRequest;
import com.hotel.trade.interfaces.response.OrderCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.hotel.trade.application.command.OrderCancelCommand;
import com.hotel.trade.interfaces.request.OrderCancelRequest;
import com.hotel.trade.interfaces.response.OrderCancelResponse;
import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.trade.application.service.TradeOperationLogService;
import org.springframework.util.StringUtils;

import java.util.function.Function;
import java.util.function.Supplier;
@RestController
@RequiredArgsConstructor
@RequestMapping("/trade/orders")
public class OrderController {
    private final OrderQueryAppService orderQueryAppService;
    private final OrderAppService orderAppService;
    private final TradeOperationLogService tradeOperationLogService;
    @PostMapping
    public ApiResponse<OrderCreateResponse> createOrder(
            @Valid @RequestBody OrderCreateRequest request
    ) {
        return monitor(
                "CREATE_ORDER",
                null,
                request.clientOrderNo(),
                () -> {
                    OrderCreateCommand command = new OrderCreateCommand(
                            request.quoteToken(),
                            request.clientOrderNo(),
                            request.contactName(),
                            request.contactPhone()
                    );

                    return orderAppService.createOrder(command);
                },
                OrderCreateResponse::orderNo
        );
    }
    @PostMapping("/{orderNo}/cancel")
    public ApiResponse<OrderCancelResponse> cancelOrder(
            @PathVariable("orderNo") String orderNo,
            @RequestBody OrderCancelRequest request
    ) {
        return monitor(
                "CANCEL_ORDER",
                orderNo,
                null,
                () -> {
                    OrderCancelCommand command = new OrderCancelCommand(
                            orderNo,
                            request.cancelReason()
                    );

                    return orderAppService.cancelOrder(command);
                },
                item -> orderNo
        );
    }

    @PostMapping("/{orderNo}/pay-success")
    public ApiResponse<OrderPaySuccessResponse> paySuccess(
            @PathVariable("orderNo") String orderNo,
            @RequestBody OrderPaySuccessRequest request
    ) {
        return monitor(
                "PAY_SUCCESS",
                orderNo,
                null,
                () -> {
                    OrderPaySuccessCommand command = new OrderPaySuccessCommand(
                            orderNo,
                            request.paymentNo()
                    );

                    return orderAppService.paySuccess(command);
                },
                item -> orderNo
        );
    }

    @PostMapping("/{orderNo}/submit-fulfillment")
    public ApiResponse<OrderFulfillmentResponse> submitFulfillment(
            @PathVariable("orderNo") String orderNo
    ) {
        return monitor(
                "SUBMIT_FULFILLMENT",
                orderNo,
                null,
                () -> orderAppService.submitFulfillment(orderNo),
                item -> orderNo
        );
    }

    @GetMapping("/{orderNo}")
    public ApiResponse<OrderDetailResponse> getOrderDetail(
            @PathVariable("orderNo") String orderNo
    ) {
        return ApiResponse.success(orderQueryAppService.getOrderDetail(orderNo));
    }

    @GetMapping("/{orderNo}/timeline")
    public ApiResponse<OrderTimelineResponse> getOrderTimeline(
            @PathVariable("orderNo") String orderNo
    ) {
        return ApiResponse.success(orderQueryAppService.getOrderTimeline(orderNo));
    }

    private <T> ApiResponse<T> monitor(
            String operationType,
            String originOrderNo,
            String clientOrderNo,
            Supplier<T> supplier,
            Function<T, String> orderNoResolver
    ) {
        long start = System.currentTimeMillis();

        try {
            T data = supplier.get();

            String finalOrderNo = StringUtils.hasText(originOrderNo)
                    ? originOrderNo
                    : orderNoResolver.apply(data);

            tradeOperationLogService.recordSuccess(
                    operationType,
                    finalOrderNo,
                    clientOrderNo,
                    System.currentTimeMillis() - start
            );

            return ApiResponse.success(data);
        } catch (BizException ex) {
            tradeOperationLogService.recordFail(
                    operationType,
                    originOrderNo,
                    clientOrderNo,
                    ex.getErrorCode().getCode(),
                    ex.getMessage(),
                    System.currentTimeMillis() - start
            );

            throw ex;
        } catch (Exception ex) {
            tradeOperationLogService.recordFail(
                    operationType,
                    originOrderNo,
                    clientOrderNo,
                    ErrorCode.SYSTEM_ERROR.getCode(),
                    ex.getMessage(),
                    System.currentTimeMillis() - start
            );

            throw ex;
        }
    }
}