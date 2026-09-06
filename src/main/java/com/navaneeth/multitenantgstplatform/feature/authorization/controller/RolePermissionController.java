package com.navaneeth.multitenantgstplatform.feature.authorization.controller;

import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.feature.authorization.dto.RolePermissionRequest;
import com.navaneeth.multitenantgstplatform.feature.authorization.dto.RolePermissionResponse;
import com.navaneeth.multitenantgstplatform.feature.authorization.service.RolePermissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;
    private final ApiResponseFactory apiResponseFactory;

    public RolePermissionController(
            RolePermissionService rolePermissionService,
            ApiResponseFactory apiResponseFactory
    ) {
        this.rolePermissionService = rolePermissionService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @GetMapping("/{roleId}/permissions")
    public ResponseEntity<ApiResponse<RolePermissionResponse>> getPermissions(
            @PathVariable UUID roleId
    ) {

        RolePermissionResponse response =
                rolePermissionService.getPermissions(roleId);

        return ResponseEntity.ok(
                apiResponseFactory.success(response, "Permissions fetched successfully")
        );
    }

    @PutMapping("/{roleId}/permissions")
    public ResponseEntity<ApiResponse<RolePermissionResponse>> updatePermissions(
            @PathVariable UUID roleId,
            @Valid @RequestBody RolePermissionRequest request
    ) {

        RolePermissionResponse response =
                rolePermissionService.updatePermissions(
                        roleId,
                        request
                );

        return ResponseEntity.ok(
                apiResponseFactory.success(response, "Permission updated successfully")
        );
    }
}