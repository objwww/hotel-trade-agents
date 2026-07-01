package com.hotel.trade.application.service;


import com.hotel.trade.infrastructure.client.fulfillment.FulfillmentClient;
import com.hotel.trade.infrastructure.client.fulfillment.request.FulfillmentConfirmRequest;
import com.hotel.trade.infrastructure.client.fulfillment.response.FulfillmentConfirmResponse;
import com.hotel.trade.interfaces.response.OrderFulfillmentResponse;
import com.hotel.trade.application.command.OrderPaySuccessCommand;
import com.hotel.trade.infrastructure.client.supply.request.InventoryConfirmRequest;
import com.hotel.trade.interfaces.response.OrderPaySuccessResponse;
import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.common.trace.TraceContext;
import com.hotel.trade.application.assembler.OrderAssembler;
import com.hotel.trade.application.command.OrderCreateCommand;
import com.hotel.trade.domain.model.SalesOrder;
import com.hotel.trade.domain.repository.SalesOrderRepository;
import com.hotel.trade.infrastructure.client.checkout.CheckoutClient;
import com.hotel.trade.infrastructure.client.checkout.response.CheckoutQuoteResponse;
import com.hotel.trade.infrastructure.client.supply.SupplyClient;
import com.hotel.trade.infrastructure.client.supply.request.InventoryFreezeRequest;
import com.hotel.trade.interfaces.response.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.hotel.trade.application.command.OrderCancelCommand;
import com.hotel.trade.infrastructure.client.supply.request.InventoryReleaseRequest;
import com.hotel.trade.interfaces.response.OrderCancelResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderAppService {

    private final SalesOrderRepository salesOrderRepository;
    private final FulfillmentClient fulfillmentClient;
    private final CheckoutClient checkoutClient;
    private final OrderStatusLogAppService orderStatusLogAppService;
    private final SupplyClient supplyClient;

    private final OrderAssembler orderAssembler;

    public OrderCreateResponse createOrder(OrderCreateCommand command) {
        validate(command);

        SalesOrder existed = salesOrderRepository.findByClientOrderNo(command.clientOrderNo())
                .orElse(null);

        if (existed != null) {
            return orderAssembler.toCreateResponse(existed);
        }

        CheckoutQuoteResponse quote = checkoutClient.getQuote(command.quoteToken());

        validateQuote(quote);

        String orderNo = generateOrderNo();
        String freezeToken = "FRZ-" + orderNo;

        supplyClient.freezeInventory(new InventoryFreezeRequest(
                freezeToken,
                orderNo,
                quote.ratePlanId(),
                quote.checkInDate(),
                quote.checkOutDate(),
                quote.roomCount()
        ));

        SalesOrder order = new SalesOrder(
                orderNo,
                quote.quoteToken(),
                command.clientOrderNo(),

                quote.hotelId(),
                quote.sellerId(),
                quote.roomTypeId(),
                quote.ratePlanId(),

                quote.checkInDate(),
                quote.checkOutDate(),
                quote.roomCount(),

                quote.totalAmount(),
                quote.currency(),
                "PENDING_PAYMENT",
                freezeToken,

                command.contactName(),
                command.contactPhone(),
                TraceContext.getTraceId(),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        salesOrderRepository.save(order);
        orderStatusLogAppService.record(
                order.getOrderNo(),
                "ORDER_CREATED",
                null,
                "PENDING_PAYMENT",
                "订单创建成功，等待支付"
        );

        orderStatusLogAppService.record(
                order.getOrderNo(),
                "INVENTORY_FROZEN",
                null,
                "PENDING_PAYMENT",
                "库存冻结成功"
        );

        return orderAssembler.toCreateResponse(order);
    }

    private void validate(OrderCreateCommand command) {
        if (!StringUtils.hasText(command.quoteToken())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "quoteToken 不能为空");
        }

        if (!StringUtils.hasText(command.clientOrderNo())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "clientOrderNo 不能为空");
        }
    }

    private void validateQuote(CheckoutQuoteResponse quote) {
        if (quote == null) {
            throw new BizException(ErrorCode.QUOTE_NOT_FOUND);
        }

        if (!"VALID".equals(quote.quoteStatus())) {
            throw new BizException(ErrorCode.QUOTE_EXPIRED, "报价状态不是 VALID");
        }

        if (quote.expireAt().isBefore(LocalDateTime.now())) {
            throw new BizException(ErrorCode.QUOTE_EXPIRED);
        }

        if (!Boolean.TRUE.equals(quote.available())) {
            throw new BizException(ErrorCode.INVENTORY_NOT_ENOUGH, "报价不可订");
        }
    }

    private String generateOrderNo() {
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "SO" + timePart + randomPart;
    }

    public OrderCancelResponse cancelOrder(OrderCancelCommand command) {
        if (!StringUtils.hasText(command.orderNo())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "orderNo 不能为空");
        }

        String cancelReason = StringUtils.hasText(command.cancelReason())
                ? command.cancelReason()
                : "订单取消";

        SalesOrder order = salesOrderRepository.findByOrderNo(command.orderNo())
                .orElseThrow(() -> new BizException(ErrorCode.ORDER_NOT_FOUND));

        if ("CANCELLED".equals(order.getOrderStatus())) {
            return orderAssembler.toCancelResponse(
                    order.getOrderNo(),
                    order.getOrderStatus(),
                    order.getFreezeToken(),
                    order.getCancelReason(),
                    order.getCancelAt()
            );
        }

        if (!"PENDING_PAYMENT".equals(order.getOrderStatus())) {
            throw new BizException(
                    ErrorCode.ORDER_CREATE_FAILED,
                    "当前订单状态不可取消：" + order.getOrderStatus()
            );
        }

        int updated = salesOrderRepository.markCancelling(order.getOrderNo());
        if (updated != 1) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "订单状态更新为取消中失败");
        }

        supplyClient.releaseInventory(new InventoryReleaseRequest(
                order.getFreezeToken(),
                order.getOrderNo()
        ));

        LocalDateTime cancelAt = LocalDateTime.now();

        int cancelled = salesOrderRepository.markCancelled(
                order.getOrderNo(),
                cancelReason,
                cancelAt
        );

        if (cancelled != 1) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "订单状态更新为已取消失败");
        }
        orderStatusLogAppService.record(
                order.getOrderNo(),
                "ORDER_CANCELLED",
                "PENDING_PAYMENT",
                "CANCELLED",
                cancelReason
        );

        return orderAssembler.toCancelResponse(
                order.getOrderNo(),
                "CANCELLED",
                order.getFreezeToken(),
                cancelReason,
                cancelAt
        );
    }

    public OrderPaySuccessResponse paySuccess(OrderPaySuccessCommand command) {
        if (!StringUtils.hasText(command.orderNo())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "orderNo 不能为空");
        }

        String paymentNo = StringUtils.hasText(command.paymentNo())
                ? command.paymentNo()
                : generatePaymentNo();

        SalesOrder order = salesOrderRepository.findByOrderNo(command.orderNo())
                .orElseThrow(() -> new BizException(ErrorCode.ORDER_NOT_FOUND));

        if ("PAID".equals(order.getOrderStatus())) {
            return orderAssembler.toPaySuccessResponse(
                    order.getOrderNo(),
                    order.getOrderStatus(),
                    order.getFreezeToken(),
                    order.getPaymentNo(),
                    order.getPaidAt()
            );
        }

        if (!"PENDING_PAYMENT".equals(order.getOrderStatus())) {
            throw new BizException(
                    ErrorCode.ORDER_CREATE_FAILED,
                    "当前订单状态不可支付：" + order.getOrderStatus()
            );
        }

        int payingUpdated = salesOrderRepository.markPaying(order.getOrderNo());

        if (payingUpdated != 1) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "订单状态更新为支付中失败");
        }

        supplyClient.confirmInventory(new InventoryConfirmRequest(
                order.getFreezeToken(),
                order.getOrderNo()
        ));

        LocalDateTime paidAt = LocalDateTime.now();

        int paidUpdated = salesOrderRepository.markPaid(
                order.getOrderNo(),
                paymentNo,
                paidAt
        );

        if (paidUpdated != 1) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "订单状态更新为已支付失败");
        }


        orderStatusLogAppService.record(
                order.getOrderNo(),
                "PAY_SUCCESS",
                "PENDING_PAYMENT",
                "PAID",
                "支付成功，订单进入已支付状态"
        );

        orderStatusLogAppService.record(
                order.getOrderNo(),
                "INVENTORY_CONFIRMED",
                "PENDING_PAYMENT",
                "PAID",
                "冻结库存确认售出"
        );

        return orderAssembler.toPaySuccessResponse(
                order.getOrderNo(),
                "PAID",
                order.getFreezeToken(),
                paymentNo,
                paidAt
        );
    }
    private String generatePaymentNo() {
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "PAY" + timePart + randomPart;
    }

    public OrderFulfillmentResponse submitFulfillment(String orderNo) {
        if (!StringUtils.hasText(orderNo)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "orderNo 不能为空");
        }

        SalesOrder order = salesOrderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BizException(ErrorCode.ORDER_NOT_FOUND));

        if ("CONFIRMED".equals(order.getOrderStatus())) {
            return orderAssembler.toFulfillmentResponse(
                    order.getOrderNo(),
                    order.getOrderStatus(),
                    order.getFulfillmentNo(),
                    order.getSupplierConfirmNo(),
                    order.getConfirmedAt()
            );
        }

        if (!"PAID".equals(order.getOrderStatus())) {
            throw new BizException(
                    ErrorCode.ORDER_CREATE_FAILED,
                    "当前订单状态不可履约：" + order.getOrderStatus()
            );
        }

        int fulfillingUpdated = salesOrderRepository.markFulfilling(order.getOrderNo());
        if (fulfillingUpdated != 1) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "订单状态更新为履约中失败");
        }

        FulfillmentConfirmResponse fulfillment = fulfillmentClient.confirm(
                new FulfillmentConfirmRequest(
                        order.getOrderNo(),

                        order.getHotelId(),
                        order.getSellerId(),
                        order.getRoomTypeId(),
                        order.getRatePlanId(),

                        order.getCheckInDate(),
                        order.getCheckOutDate(),
                        order.getRoomCount(),

                        order.getContactName(),
                        order.getContactPhone()
                )
        );

        LocalDateTime confirmedAt = LocalDateTime.now();

        int confirmedUpdated = salesOrderRepository.markConfirmed(
                order.getOrderNo(),
                fulfillment.fulfillmentNo(),
                fulfillment.supplierConfirmNo(),
                confirmedAt
        );

        if (confirmedUpdated != 1) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "订单状态更新为已确认失败");
        }
        if (confirmedUpdated != 1) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "订单状态更新为已确认失败");
        }

        orderStatusLogAppService.record(
                order.getOrderNo(),
                "FULFILLMENT_CONFIRMED",
                "PAID",
                "CONFIRMED",
                "供应商履约确认成功"
        );

        return orderAssembler.toFulfillmentResponse(
                order.getOrderNo(),
                "CONFIRMED",
                fulfillment.fulfillmentNo(),
                fulfillment.supplierConfirmNo(),
                confirmedAt
        );
    }
}