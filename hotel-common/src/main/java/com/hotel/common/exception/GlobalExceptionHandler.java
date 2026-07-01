package com.hotel.common.exception;

import com.hotel.common.api.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestControllerAdvice(basePackages = "com.hotel")
public class GlobalExceptionHandler {

    private static final MediaType JSON_UTF8 =
            new MediaType("application", "json", StandardCharsets.UTF_8);

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ApiResponse<Void>> handleBizException(BizException ex) {
        log.warn("Business exception, code={}, message={}",
                ex.getErrorCode().getCode(),
                ex.getMessage()
        );

        return ResponseEntity.ok()
                .contentType(JSON_UTF8)
                .body(ApiResponse.fail(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleParamException(Exception ex) {
        log.warn("Parameter invalid, message={}", ex.getMessage());

        return ResponseEntity.ok()
                .contentType(JSON_UTF8)
                .body(ApiResponse.fail(ErrorCode.PARAM_INVALID, "请求参数不合法"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("System error", ex);

        return ResponseEntity.ok()
                .contentType(JSON_UTF8)
                .body(ApiResponse.fail(ErrorCode.SYSTEM_ERROR));
    }
}