package com.hotel.trade.domain.repository;

import com.hotel.trade.domain.model.SalesOrder;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SalesOrderRepository {

    void save(SalesOrder salesOrder);

    Optional<SalesOrder> findByClientOrderNo(String clientOrderNo);

    Optional<SalesOrder> findByOrderNo(String orderNo);

    int markCancelling(String orderNo);

    int markCancelled(String orderNo, String cancelReason, LocalDateTime cancelAt);
    int markPaying(String orderNo);

    int markPaid(String orderNo, String paymentNo, LocalDateTime paidAt);
    int markFulfilling(String orderNo);

    int markConfirmed(
            String orderNo,
            String fulfillmentNo,
            String supplierConfirmNo,
            LocalDateTime confirmedAt
    );






}