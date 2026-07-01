package com.hotel.checkout.infrastructure.client.supply;

import com.hotel.checkout.infrastructure.client.supply.response.SupplyAvailabilityResponse;
import com.hotel.common.api.ApiResponse;
import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.common.trace.TraceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SupplyClient {

    private final RestTemplate restTemplate;

    @Value("${hotel.supply.base-url}")
    private String supplyBaseUrl;

    public SupplyAvailabilityResponse queryAvailability(
            String ratePlanId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Integer roomCount
    ) {
        String url = UriComponentsBuilder.fromHttpUrl(supplyBaseUrl)
                .path("/supply/rate-plans/{ratePlanId}/availability")
                .queryParam("checkInDate", checkInDate)
                .queryParam("checkOutDate", checkOutDate)
                .queryParam("roomCount", roomCount)
                .buildAndExpand(ratePlanId)
                .toUriString();

        ResponseEntity<ApiResponse<SupplyAvailabilityResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                buildHttpEntity(),
                new ParameterizedTypeReference<>() {
                }
        );

        ApiResponse<SupplyAvailabilityResponse> body = response.getBody();

        if (body == null) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "调用 Supply 可订性接口失败：响应为空");
        }

        if (!Boolean.TRUE.equals(body.getSuccess())) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "调用 Supply 可订性接口失败：" + body.getMessage());
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