package com.navaneeth.multitenantgstplatform.feature.master.controller;

import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateHsnCodeRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.HsnCodeResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateHsnCodeRequest;
import com.navaneeth.multitenantgstplatform.feature.master.service.HsnCodeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hsn-codes")
public class HsnCodeController {

    private final HsnCodeService hsnCodeService;
    private final ApiResponseFactory apiResponseFactory;

    public HsnCodeController(
            HsnCodeService hsnCodeService,
            ApiResponseFactory apiResponseFactory
    ) {
        this.hsnCodeService = hsnCodeService;
        this.apiResponseFactory = apiResponseFactory;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<HsnCodeResponse>> createHsnCode(
            @Valid @RequestBody CreateHsnCodeRequest request
    ) {

        HsnCodeResponse response =
                hsnCodeService.createHsnCode(request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "HSN code created successfully"
                )
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<HsnCodeResponse>>> getHsnCodes() {

        List<HsnCodeResponse> response =
                hsnCodeService.getHsnCodes();

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "HSN codes retrieved successfully"
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HsnCodeResponse>> getHsnCode(
            @PathVariable UUID id
    ) {

        HsnCodeResponse response =
                hsnCodeService.getHsnCode(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "HSN code retrieved successfully"
                )
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HsnCodeResponse>> updateHsnCode(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateHsnCodeRequest request
    ) {

        HsnCodeResponse response =
                hsnCodeService.updateHsnCode(id, request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "HSN code updated successfully"
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHsnCode(
            @PathVariable UUID id
    ) {

        hsnCodeService.deleteHsnCode(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        null,
                        "HSN code deleted successfully"
                )
        );
    }
}