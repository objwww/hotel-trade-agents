package com.hotel.trade.application.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.common.trace.TraceContext;
import com.hotel.trade.infrastructure.persistence.mapper.OrderStatusLogMapper;
import com.hotel.trade.infrastructure.persistence.po.OrderStatusLogPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusLogAppService {

    private final OrderStatusLogMapper orderStatusLogMapper;

    public void record(
            String orderNo,
            String eventType,
            String fromStatus,
            String toStatus,
            String eventDesc
    ) {
        OrderStatusLogPO po = new OrderStatusLogPO();
        po.setOrderNo(orderNo);
        po.setEventType(eventType);
        po.setFromStatus(fromStatus);
        po.setToStatus(toStatus);
        po.setEventDesc(eventDesc);
        po.setSourceTraceId(TraceContext.getTraceId());

        orderStatusLogMapper.insert(po);
    }

    public List<OrderStatusLogPO> listByOrderNo(String orderNo) {
        return orderStatusLogMapper.selectList(
                Wrappers.<OrderStatusLogPO>lambdaQuery()
                        .eq(OrderStatusLogPO::getOrderNo, orderNo)
                        .orderByAsc(OrderStatusLogPO::getCreatedAt)
                        .orderByAsc(OrderStatusLogPO::getId)
        );
    }
}