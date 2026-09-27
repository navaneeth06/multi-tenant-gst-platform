package com.navaneeth.multitenantgstplatform.feature.master.controller;

import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateGSTRateRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.GSTRateResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateGSTRateRequest;
import com.navaneeth.multitenantgstplatform.feature.master.service.GSTRateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/gst-rates")
public class GSTRateController {

    private final GSTRateService gstRateService;
    private final ApiResponseFactory apiResponseFactory;

    public GSTRateController(
            GSTRateService gstRateService,
            ApiResponseFactory apiResponseFactory
    ) {
        this.gstRateService = gstRateService;
        this.apiResponseFactory = apiResponseFactory;
    }


    // Admin only — restrict via your existing security convention (method
    // security / role check), matching how other write endpoints in this
    // codebase enforce ADMIN-only access.
    @PostMapping
    public ResponseEntity<ApiResponse<GSTRateResponse>> createRate(
            @Valid @RequestBody CreateGSTRateRequest request
    ) {

        GSTRateResponse response =
                gstRateService.createRate(request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "GST rate created successfully"
                )
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<GSTRateResponse>>> getRates() {

        List<GSTRateResponse> response =
                gstRateService.getRates();

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "GST rates retrieved successfully"
                )
        );
    }


    // Convenience endpoint for dropdowns (e.g. product creation) that should
    // only ever offer currently-usable rates.
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<GSTRateResponse>>> getActiveRates() {

        List<GSTRateResponse> response =
                gstRateService.getActiveRates();

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Active GST rates retrieved successfully"
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GSTRateResponse>> getRate(
            @PathVariable UUID id
    ) {

        GSTRateResponse response =
                gstRateService.getRate(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "GST rate retrieved successfully"
                )
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GSTRateResponse>> updateRate(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGSTRateRequest request
    ) {

        GSTRateResponse response =
                gstRateService.updateRate(id, request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "GST rate updated successfully"
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateRate(
            @PathVariable UUID id
    ) {

        gstRateService.deactivateRate(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        null,
                        "GST rate deactivated successfully"
                )
        );
    }
}