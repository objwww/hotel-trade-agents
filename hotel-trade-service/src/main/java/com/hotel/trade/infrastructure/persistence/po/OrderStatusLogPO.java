package com.hotel.trade.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order_status_log")
public class OrderStatusLogPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private String eventType;

    private String fromStatus;

    private String toStatus;

    private String eventDesc;

    private String sourceTraceId;

    private LocalDateTime createdAt;
}