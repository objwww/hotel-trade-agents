package com.hotel.trade.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.trade.infrastructure.persistence.po.SalesOrderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;


@Mapper
public interface SalesOrderMapper extends BaseMapper<SalesOrderPO> {

    @Update("""
            UPDATE sales_order
            SET order_status = 'CANCELLING'
            WHERE order_no = #{orderNo}
              AND order_status = 'PENDING_PAYMENT'
            """)
    int markCancelling(@Param("orderNo") String orderNo);

    @Update("""
            UPDATE sales_order
            SET order_status = 'CANCELLED',
                cancel_reason = #{cancelReason},
                cancel_at = #{cancelAt}
            WHERE order_no = #{orderNo}
              AND order_status = 'CANCELLING'
            """)
    int markCancelled(
            @Param("orderNo") String orderNo,
            @Param("cancelReason") String cancelReason,
            @Param("cancelAt") LocalDateTime cancelAt
    );

    //CANCELLING 是为了避免两个请求同时取消同一个订单

    @Update("""
        UPDATE sales_order
        SET order_status = 'PAYING'
        WHERE order_no = #{orderNo}
          AND order_status = 'PENDING_PAYMENT'
        """)
    int markPaying(@Param("orderNo") String orderNo);

    @Update("""
        UPDATE sales_order
        SET order_status = 'PAID',
            payment_no = #{paymentNo},
            paid_at = #{paidAt}
        WHERE order_no = #{orderNo}
          AND order_status = 'PAYING'
        """)
    int markPaid(
            @Param("orderNo") String orderNo,
            @Param("paymentNo") String paymentNo,
            @Param("paidAt") LocalDateTime paidAt
    );


    @Update("""
        UPDATE sales_order
        SET order_status = 'FULFILLING'
        WHERE order_no = #{orderNo}
          AND order_status = 'PAID'
        """)
    int markFulfilling(@Param("orderNo") String orderNo);

    @Update("""
        UPDATE sales_order
        SET order_status = 'CONFIRMED',
            fulfillment_no = #{fulfillmentNo},
            supplier_confirm_no = #{supplierConfirmNo},
            confirmed_at = #{confirmedAt}
        WHERE order_no = #{orderNo}
          AND order_status = 'FULFILLING'
        """)
    int markConfirmed(
            @Param("orderNo") String orderNo,
            @Param("fulfillmentNo") String fulfillmentNo,
            @Param("supplierConfirmNo") String supplierConfirmNo,
            @Param("confirmedAt") LocalDateTime confirmedAt
    );

}