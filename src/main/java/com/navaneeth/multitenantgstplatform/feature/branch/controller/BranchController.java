package com.navaneeth.multitenantgstplatform.feature.branch.controller;

import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.feature.branch.dto.BranchResponse;
import com.navaneeth.multitenantgstplatform.feature.branch.dto.CreateBranchRequest;
import com.navaneeth.multitenantgstplatform.feature.branch.dto.UpdateBranchRequest;
import com.navaneeth.multitenantgstplatform.feature.branch.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {

    private final BranchService branchService;
    private final ApiResponseFactory apiResponseFactory;

    public BranchController(
            BranchService branchService,
            ApiResponseFactory apiResponseFactory
    ) {
        this.branchService = branchService;
        this.apiResponseFactory = apiResponseFactory;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(
            @Valid @RequestBody CreateBranchRequest request
    ) {

        BranchResponse response =
                branchService.createBranch(request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Branch created successfully"
                )
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getBranches() {

        List<BranchResponse> response =
                branchService.getBranches();

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Branches retrieved successfully"
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchResponse>> getBranch(
            @PathVariable UUID id
    ) {

        BranchResponse response =
                branchService.getBranch(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Branch retrieved successfully"
                )
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchResponse>> updateBranch(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBranchRequest request
    ) {

        BranchResponse response =
                branchService.updateBranch(id, request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Branch updated successfully"
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateBranch(
            @PathVariable UUID id
    ) {

        branchService.deactivateBranch(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        null,
                        "Branch deactivated successfully"
                )
        );
    }
}