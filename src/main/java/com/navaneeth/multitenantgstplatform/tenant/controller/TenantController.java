package com.navaneeth.multitenantgstplatform.tenant.controller;

import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.tenant.dto.CreateTenantRequest;
import com.navaneeth.multitenantgstplatform.tenant.dto.TenantResponse;
import com.navaneeth.multitenantgstplatform.tenant.dto.UpdateTenantRequest;
import com.navaneeth.multitenantgstplatform.tenant.service.TenantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TenantResponse>>> getAllTenants() {
        List<TenantResponse> tenants = tenantService.getAllTenants();
        ApiResponse<List<TenantResponse>> apiResponse = ApiResponse.<List<TenantResponse>>builder()
                .success(true)
                .data(tenants)
                .message("Tenant fetched successfully")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TenantResponse>> createTenant(
            @RequestBody CreateTenantRequest request) {

        TenantResponse response = tenantService.createTenant(request);

        ApiResponse<TenantResponse> apiResponse =
                ApiResponse.<TenantResponse>builder()
                        .success(true)
                        .message("Tenants created successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TenantResponse>> updateTenant(
            @PathVariable UUID id,
            @RequestBody UpdateTenantRequest request) {

        TenantResponse response = tenantService.updateTenant(id, request);

        ApiResponse<TenantResponse> apiResponse =
                ApiResponse.<TenantResponse>builder()
                        .success(true)
                        .message("Tenants updated successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(apiResponse);

    }

    @PatchMapping("/toggle/{id}")
    public ResponseEntity<ApiResponse<TenantResponse>> toggleTenantStatus(@PathVariable UUID id) {
        TenantResponse response = tenantService.toggleTenantStatus(id);
        String message = response.isActive()
                ? "Tenant unblocked successfully."
                : "Tenant blocked successfully.";

        ApiResponse<TenantResponse> apiResponse = ApiResponse.<TenantResponse>builder()
                .success(true)
                .message(message)
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}