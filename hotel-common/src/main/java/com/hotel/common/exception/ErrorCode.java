package com.hotel.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS("SUCCESS", "success"),

    PARAM_INVALID("PARAM_INVALID", "请求参数不合法"),

    HOTEL_NOT_FOUND("HOTEL_NOT_FOUND", "酒店不存在"),

    RATE_PLAN_NOT_FOUND("RATE_PLAN_NOT_FOUND", "售卖计划不存在"),

    DATE_RANGE_INVALID("DATE_RANGE_INVALID", "入住日期范围不合法"),

    PRICE_NOT_FOUND("PRICE_NOT_FOUND", "指定日期缺少价格"),

    INVENTORY_NOT_ENOUGH("INVENTORY_NOT_ENOUGH", "库存不足"),
    QUOTE_NOT_FOUND("QUOTE_NOT_FOUND", "报价不存在"),
    QUOTE_EXPIRED("QUOTE_EXPIRED", "报价已过期"),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "订单不存在"),
    ORDER_CREATE_FAILED("ORDER_CREATE_FAILED", "订单创建失败"),
    INVENTORY_FREEZE_FAILED("INVENTORY_FREEZE_FAILED", "库存冻结失败"),

    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常");


    private final String code;

    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}