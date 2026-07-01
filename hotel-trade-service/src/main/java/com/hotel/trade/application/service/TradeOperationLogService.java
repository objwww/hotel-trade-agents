package com.hotel.trade.application.service;

import com.hotel.common.trace.TraceContext;
import com.hotel.trade.infrastructure.persistence.mapper.TradeOperationLogMapper;
import com.hotel.trade.infrastructure.persistence.po.TradeOperationLogPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeOperationLogService {

    private final TradeOperationLogMapper tradeOperationLogMapper;

    public void recordSuccess(
            String operationType,
            String orderNo,
            String clientOrderNo,
            long costMs
    ) {
        TradeOperationLogPO po = new TradeOperationLogPO();
        po.setOperationType(operationType);
        po.setOrderNo(orderNo);
        po.setClientOrderNo(clientOrderNo);
        po.setTraceId(TraceContext.getTraceId());
        po.setSuccess(true);
        po.setCostMs(costMs);

        tradeOperationLogMapper.insert(po);
    }

    public void recordFail(
            String operationType,
            String orderNo,
            String clientOrderNo,
            String errorCode,
            String errorMessage,
            long costMs
    ) {
        TradeOperationLogPO po = new TradeOperationLogPO();
        po.setOperationType(operationType);
        po.setOrderNo(orderNo);
        po.setClientOrderNo(clientOrderNo);
        po.setTraceId(TraceContext.getTraceId());
        po.setSuccess(false);
        po.setErrorCode(errorCode);
        po.setErrorMessage(errorMessage);
        po.setCostMs(costMs);

        tradeOperationLogMapper.insert(po);
    }
}