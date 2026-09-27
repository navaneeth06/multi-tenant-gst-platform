package com.navaneeth.multitenantgstplatform.feature.master.controller;

import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateUnitRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UnitResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateUnitRequest;
import com.navaneeth.multitenantgstplatform.feature.master.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/units")
public class UnitController {

    private final UnitService unitService;
    private final ApiResponseFactory apiResponseFactory;

    public UnitController(
            UnitService unitService,
            ApiResponseFactory apiResponseFactory
    ) {
        this.unitService = unitService;
        this.apiResponseFactory = apiResponseFactory;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<UnitResponse>> createUnit(
            @Valid @RequestBody CreateUnitRequest request
    ) {

        UnitResponse response =
                unitService.createUnit(request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Unit created successfully"
                )
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<UnitResponse>>> getUnits() {

        List<UnitResponse> response =
                unitService.getUnits();

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Units retrieved successfully"
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitResponse>> getUnit(
            @PathVariable UUID id
    ) {

        UnitResponse response =
                unitService.getUnit(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Unit retrieved successfully"
                )
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitResponse>> updateUnit(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUnitRequest request
    ) {

        UnitResponse response =
                unitService.updateUnit(id, request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Unit updated successfully"
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUnit(
            @PathVariable UUID id
    ) {

        unitService.deleteUnit(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        null,
                        "Unit deleted successfully"
                )
        );
    }
}