package com.hotel.guide.infrastructure.client.supply;

import com.hotel.common.api.ApiResponse;
import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.common.trace.TraceContext;
import com.hotel.guide.infrastructure.client.supply.response.SupplyAvailabilityResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyHotelResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyRatePlanResponse;
import com.hotel.guide.infrastructure.client.supply.response.SupplyRoomTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SupplyClient {

    private final RestTemplate restTemplate;

    @Value("${hotel.supply.base-url}")
    private String supplyBaseUrl;

    public SupplyHotelResponse getHotel(String hotelId) {
        String url = supplyBaseUrl + "/supply/hotels/" + hotelId;

        ResponseEntity<ApiResponse<SupplyHotelResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                buildHttpEntity(),
                new ParameterizedTypeReference<>() {
                }
        );

        return getDataOrThrow(response.getBody(), "查询酒店详情失败");
    }

    public List<SupplyRoomTypeResponse> listRooms(String hotelId) {
        String url = supplyBaseUrl + "/supply/hotels/" + hotelId + "/rooms";

        ResponseEntity<ApiResponse<List<SupplyRoomTypeResponse>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                buildHttpEntity(),
                new ParameterizedTypeReference<>() {
                }
        );

        return getDataOrThrow(response.getBody(), "查询房型失败");
    }

    public List<SupplyRatePlanResponse> listRatePlans(String hotelId) {
        String url = supplyBaseUrl + "/supply/hotels/" + hotelId + "/rate-plans";

        ResponseEntity<ApiResponse<List<SupplyRatePlanResponse>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                buildHttpEntity(),
                new ParameterizedTypeReference<>() {
                }
        );

        return getDataOrThrow(response.getBody(), "查询售卖计划失败");
    }

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

        return getDataOrThrow(response.getBody(), "查询实时可订性失败");
    }

    private HttpEntity<Void> buildHttpEntity() {
        HttpHeaders headers = new HttpHeaders();

        String traceId = TraceContext.getTraceId();
        if (StringUtils.hasText(traceId)) {
            headers.set("X-Trace-Id", traceId);
        }

        return new HttpEntity<>(headers);
    }

    private <T> T getDataOrThrow(ApiResponse<T> body, String message) {
        if (body == null) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, message + ": 响应为空");
        }

        if (!Boolean.TRUE.equals(body.getSuccess())) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, message + ": " + body.getMessage());
        }

        return body.getData();
    }
}