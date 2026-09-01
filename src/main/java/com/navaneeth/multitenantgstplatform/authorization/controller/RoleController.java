package com.navaneeth.multitenantgstplatform.authorization.controller;

import com.navaneeth.multitenantgstplatform.authorization.dto.CreateRoleRequest;
import com.navaneeth.multitenantgstplatform.authorization.dto.RoleResponse;
import com.navaneeth.multitenantgstplatform.authorization.dto.UpdateRoleRequest;
import com.navaneeth.multitenantgstplatform.authorization.service.RoleService;
import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;
    private final ApiResponseFactory apiResponseFactory;

    public RoleController(
            RoleService roleService,
            ApiResponseFactory apiResponseFactory
    ) {
        this.roleService = roleService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(
            @Valid @RequestBody CreateRoleRequest request
    ) {
        RoleResponse response = roleService.createRole(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponseFactory.success(
                        response,
                        "Role created successfully"
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getRoles() {

        List<RoleResponse> response = roleService.getRoles();

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Roles retrieved successfully"
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRoleRequest request
    ) {
        RoleResponse response =
                roleService.updateRole(id, request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Role updated successfully"
                )
        );
    }
}