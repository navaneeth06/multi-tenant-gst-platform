package com.navaneeth.multitenantgstplatform.authentication.controller;

import com.navaneeth.multitenantgstplatform.authentication.dto.LoginRequest;
import com.navaneeth.multitenantgstplatform.authentication.dto.LoginResponse;
import com.navaneeth.multitenantgstplatform.authentication.service.AuthService;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @RequestBody LoginRequest request
    ) {

        LoginResponse response = authService.login(
                tenantId,
                request
        );

        ApiResponse<LoginResponse> apiResponse =
                ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
}