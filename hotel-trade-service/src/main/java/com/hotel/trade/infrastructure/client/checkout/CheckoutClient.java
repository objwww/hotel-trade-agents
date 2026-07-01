package com.hotel.trade.infrastructure.client.checkout;

import com.hotel.common.api.ApiResponse;
import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.common.trace.TraceContext;
import com.hotel.trade.infrastructure.client.checkout.response.CheckoutQuoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CheckoutClient {

    private final RestTemplate restTemplate;

    @Value("${hotel.checkout.base-url}")
    private String checkoutBaseUrl;

    public CheckoutQuoteResponse getQuote(String quoteToken) {
        String url = checkoutBaseUrl + "/checkout/quotes/" + quoteToken;

        ResponseEntity<ApiResponse<CheckoutQuoteResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                buildHttpEntity(),
                new ParameterizedTypeReference<>() {
                }
        );

        ApiResponse<CheckoutQuoteResponse> body = response.getBody();

        if (body == null) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "查询报价失败：响应为空");
        }

        if (!Boolean.TRUE.equals(body.getSuccess())) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "查询报价失败：" + body.getMessage());
        }

        return body.getData();
    }

    private HttpEntity<Void> buildHttpEntity() {
        HttpHeaders headers = new HttpHeaders();

        String traceId = TraceContext.getTraceId();
        if (StringUtils.hasText(traceId)) {
            headers.set("X-Trace-Id", traceId);
        }

        return new HttpEntity<>(headers);
    }
}