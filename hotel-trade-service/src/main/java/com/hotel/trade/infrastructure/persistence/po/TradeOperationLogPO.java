package com.hotel.trade.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("trade_operation_log")
public class TradeOperationLogPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String operationType;

    private String orderNo;

    private String clientOrderNo;

    private String traceId;

    private Boolean success;

    private String errorCode;

    private String errorMessage;

    private Long costMs;

    private LocalDateTime createdAt;
}