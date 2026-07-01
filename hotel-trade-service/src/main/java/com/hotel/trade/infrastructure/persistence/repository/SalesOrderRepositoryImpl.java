package com.hotel.trade.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.trade.domain.model.SalesOrder;
import com.hotel.trade.domain.repository.SalesOrderRepository;
import com.hotel.trade.infrastructure.persistence.mapper.SalesOrderMapper;
import com.hotel.trade.infrastructure.persistence.po.SalesOrderPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SalesOrderRepositoryImpl implements SalesOrderRepository {

    private final SalesOrderMapper salesOrderMapper;

    @Override
    public void save(SalesOrder salesOrder) {
        salesOrderMapper.insert(toPO(salesOrder));
    }

    @Override
    public Optional<SalesOrder> findByClientOrderNo(String clientOrderNo) {
        SalesOrderPO po = salesOrderMapper.selectOne(
                Wrappers.<SalesOrderPO>lambdaQuery()
                        .eq(SalesOrderPO::getClientOrderNo, clientOrderNo)
        );

        return Optional.ofNullable(po).map(this::toDomain);
    }

    private SalesOrderPO toPO(SalesOrder order) {
        SalesOrderPO po = new SalesOrderPO();

        po.setOrderNo(order.getOrderNo());
        po.setQuoteToken(order.getQuoteToken());
        po.setClientOrderNo(order.getClientOrderNo());

        po.setHotelId(order.getHotelId());
        po.setSellerId(order.getSellerId());
        po.setRoomTypeId(order.getRoomTypeId());
        po.setRatePlanId(order.getRatePlanId());

        po.setCheckInDate(order.getCheckInDate());
        po.setCheckOutDate(order.getCheckOutDate());
        po.setRoomCount(order.getRoomCount());

        po.setOrderAmount(order.getOrderAmount());
        po.setCurrency(order.getCurrency());
        po.setOrderStatus(order.getOrderStatus());
        po.setFreezeToken(order.getFreezeToken());

        po.setContactName(order.getContactName());
        po.setContactPhone(order.getContactPhone());
        po.setSourceTraceId(order.getSourceTraceId());
        po.setCancelReason(order.getCancelReason());
        po.setCancelAt(order.getCancelAt());
        po.setPaymentNo(order.getPaymentNo());
        po.setPaidAt(order.getPaidAt());
        po.setFulfillmentNo(order.getFulfillmentNo());
        po.setSupplierConfirmNo(order.getSupplierConfirmNo());
        po.setConfirmedAt(order.getConfirmedAt());
        return po;
    }

    @Override
    public Optional<SalesOrder> findByOrderNo(String orderNo) {
        SalesOrderPO po = salesOrderMapper.selectOne(
                Wrappers.<SalesOrderPO>lambdaQuery()
                        .eq(SalesOrderPO::getOrderNo, orderNo)
        );

        return Optional.ofNullable(po).map(this::toDomain);
    }

    @Override
    public int markCancelling(String orderNo) {
        return salesOrderMapper.markCancelling(orderNo);
    }

    @Override
    public int markCancelled(String orderNo, String cancelReason, LocalDateTime cancelAt) {
        return salesOrderMapper.markCancelled(orderNo, cancelReason, cancelAt);
    }

    private SalesOrder toDomain(SalesOrderPO po) {
        return new SalesOrder(
                po.getOrderNo(),
                po.getQuoteToken(),
                po.getClientOrderNo(),

                po.getHotelId(),
                po.getSellerId(),
                po.getRoomTypeId(),
                po.getRatePlanId(),

                po.getCheckInDate(),
                po.getCheckOutDate(),
                po.getRoomCount(),

                po.getOrderAmount(),
                po.getCurrency(),
                po.getOrderStatus(),
                po.getFreezeToken(),

                po.getContactName(),
                po.getContactPhone(),
                po.getSourceTraceId(),
                po.getCancelReason(),
                po.getCancelAt(),
                po.getPaymentNo(),
                po.getPaidAt(),
                po.getFulfillmentNo(),
                po.getSupplierConfirmNo(),
                po.getConfirmedAt()
        );

    }
    @Override
    public int markPaying(String orderNo) {
        return salesOrderMapper.markPaying(orderNo);
    }

    @Override
    public int markPaid(String orderNo, String paymentNo, LocalDateTime paidAt) {
        return salesOrderMapper.markPaid(orderNo, paymentNo, paidAt);
    }

    @Override
    public int markFulfilling(String orderNo) {
        return salesOrderMapper.markFulfilling(orderNo);
    }

    @Override
    public int markConfirmed(
            String orderNo,
            String fulfillmentNo,
            String supplierConfirmNo,
            LocalDateTime confirmedAt
    ) {
        return salesOrderMapper.markConfirmed(
                orderNo,
                fulfillmentNo,
                supplierConfirmNo,
                confirmedAt
        );
    }
}