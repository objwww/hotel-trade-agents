package com.hotel.trade.infrastructure.client.supply;
import com.hotel.trade.infrastructure.client.supply.request.InventoryConfirmRequest;
import com.hotel.trade.infrastructure.client.supply.response.InventoryConfirmResponse;
import com.hotel.common.api.ApiResponse;
import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.common.trace.TraceContext;
import com.hotel.trade.infrastructure.client.supply.request.InventoryFreezeRequest;
import com.hotel.trade.infrastructure.client.supply.request.InventoryReleaseRequest;
import com.hotel.trade.infrastructure.client.supply.response.InventoryFreezeResponse;
import com.hotel.trade.infrastructure.client.supply.response.InventoryReleaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SupplyClient {

    private final RestTemplate restTemplate;

    @Value("${hotel.supply.base-url}")
    private String supplyBaseUrl;

    public InventoryFreezeResponse freezeInventory(InventoryFreezeRequest request) {
        String url = supplyBaseUrl + "/supply/inventories/freeze";

        ResponseEntity<ApiResponse<InventoryFreezeResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                buildHttpEntity(request),
                new ParameterizedTypeReference<>() {
                }
        );

        ApiResponse<InventoryFreezeResponse> body = response.getBody();

        if (body == null) {
            throw new BizException(ErrorCode.INVENTORY_FREEZE_FAILED, "库存冻结失败：响应为空");
        }

        if (!Boolean.TRUE.equals(body.getSuccess())) {
            throw new BizException(ErrorCode.INVENTORY_FREEZE_FAILED, "库存冻结失败：" + body.getMessage());
        }

        return body.getData();
    }

    private HttpEntity<InventoryFreezeRequest> buildHttpEntity(InventoryFreezeRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String traceId = TraceContext.getTraceId();
        if (StringUtils.hasText(traceId)) {
            headers.set("X-Trace-Id", traceId);
        }

        return new HttpEntity<>(request, headers);
    }

    public InventoryReleaseResponse releaseInventory(InventoryReleaseRequest request) {
        String url = supplyBaseUrl + "/supply/inventories/release";

        ResponseEntity<ApiResponse<InventoryReleaseResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                buildReleaseHttpEntity(request),
                new ParameterizedTypeReference<>() {
                }
        );

        ApiResponse<InventoryReleaseResponse> body = response.getBody();

        if (body == null) {
            throw new BizException(ErrorCode.INVENTORY_FREEZE_FAILED, "释放库存失败：响应为空");
        }

        if (!Boolean.TRUE.equals(body.getSuccess())) {
            throw new BizException(ErrorCode.INVENTORY_FREEZE_FAILED, "释放库存失败：" + body.getMessage());
        }

        return body.getData();
    }

    private HttpEntity<InventoryReleaseRequest> buildReleaseHttpEntity(InventoryReleaseRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String traceId = TraceContext.getTraceId();
        if (StringUtils.hasText(traceId)) {
            headers.set("X-Trace-Id", traceId);
        }

        return new HttpEntity<>(request, headers);
    }

    public InventoryConfirmResponse confirmInventory(InventoryConfirmRequest request) {
        String url = supplyBaseUrl + "/supply/inventories/confirm";

        ResponseEntity<ApiResponse<InventoryConfirmResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                buildConfirmHttpEntity(request),
                new ParameterizedTypeReference<>() {
                }
        );

        ApiResponse<InventoryConfirmResponse> body = response.getBody();

        if (body == null) {
            throw new BizException(ErrorCode.INVENTORY_FREEZE_FAILED, "确认库存失败：响应为空");
        }

        if (!Boolean.TRUE.equals(body.getSuccess())) {
            throw new BizException(ErrorCode.INVENTORY_FREEZE_FAILED, "确认库存失败：" + body.getMessage());
        }

        return body.getData();
    }

    private HttpEntity<InventoryConfirmRequest> buildConfirmHttpEntity(InventoryConfirmRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String traceId = TraceContext.getTraceId();
        if (StringUtils.hasText(traceId)) {
            headers.set("X-Trace-Id", traceId);
        }

        return new HttpEntity<>(request, headers);
    }
}