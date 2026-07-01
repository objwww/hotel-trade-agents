package com.hotel.trade.infrastructure.client.fulfillment;

import com.hotel.common.api.ApiResponse;
import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.common.trace.TraceContext;
import com.hotel.trade.infrastructure.client.fulfillment.request.FulfillmentConfirmRequest;
import com.hotel.trade.infrastructure.client.fulfillment.response.FulfillmentConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class FulfillmentClient {

    private final RestTemplate restTemplate;

    @Value("${hotel.fulfillment.base-url}")
    private String fulfillmentBaseUrl;

    public FulfillmentConfirmResponse confirm(FulfillmentConfirmRequest request) {
        String url = fulfillmentBaseUrl + "/fulfillment/orders/confirm";

        ResponseEntity<ApiResponse<FulfillmentConfirmResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                buildHttpEntity(request),
                new ParameterizedTypeReference<>() {
                }
        );

        ApiResponse<FulfillmentConfirmResponse> body = response.getBody();

        if (body == null) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "履约确认失败：响应为空");
        }

        if (!Boolean.TRUE.equals(body.getSuccess())) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "履约确认失败：" + body.getMessage());
        }

        return body.getData();
    }

    private HttpEntity<FulfillmentConfirmRequest> buildHttpEntity(FulfillmentConfirmRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String traceId = TraceContext.getTraceId();
        if (StringUtils.hasText(traceId)) {
            headers.set("X-Trace-Id", traceId);
        }

        return new HttpEntity<>(request, headers);
    }
}