package com.hotel.supply.interfaces.controller;

import com.hotel.common.api.ApiResponse;
import com.hotel.supply.application.service.InventoryConfirmAppService;
import com.hotel.supply.application.service.InventoryFreezeAppService;
import com.hotel.supply.application.service.InventoryReleaseAppService;
import com.hotel.supply.interfaces.request.InventoryConfirmRequest;
import com.hotel.supply.interfaces.request.InventoryFreezeRequest;
import com.hotel.supply.interfaces.request.InventoryReleaseRequest;
import com.hotel.supply.interfaces.response.InventoryConfirmResponse;
import com.hotel.supply.interfaces.response.InventoryFreezeResponse;
import com.hotel.supply.interfaces.response.InventoryReleaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supply/inventories")
public class InventoryController {

    private final InventoryFreezeAppService inventoryFreezeAppService;

    private final InventoryReleaseAppService inventoryReleaseAppService;

    private final InventoryConfirmAppService inventoryConfirmAppService;

    @PostMapping("/freeze")
    public ApiResponse<InventoryFreezeResponse> freeze(
            @RequestBody InventoryFreezeRequest request
    ) {
        return ApiResponse.success(inventoryFreezeAppService.freeze(request));
    }

    @PostMapping("/release")
    public ApiResponse<InventoryReleaseResponse> release(
            @RequestBody InventoryReleaseRequest request
    ) {
        return ApiResponse.success(inventoryReleaseAppService.release(request));
    }

    @PostMapping("/confirm")
    public ApiResponse<InventoryConfirmResponse> confirm(
            @RequestBody InventoryConfirmRequest request
    ) {
        return ApiResponse.success(inventoryConfirmAppService.confirm(request));
    }
}