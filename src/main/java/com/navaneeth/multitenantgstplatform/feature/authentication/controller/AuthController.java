package com.navaneeth.multitenantgstplatform.feature.authentication.controller;

import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.LoginRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.LoginResponse;
import com.navaneeth.multitenantgstplatform.feature.authentication.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginRequest request
    ) {

        LoginResponse response =
                authService.login(request);

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
