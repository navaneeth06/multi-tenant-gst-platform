package com.navaneeth.multitenantgstplatform.feature.authentication.controller;

import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.CreateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UpdateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UserResponse;
import com.navaneeth.multitenantgstplatform.feature.authentication.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final ApiResponseFactory apiResponseFactory;

    public UserController(
            UserService userService,
            ApiResponseFactory apiResponseFactory
    ) {
        this.userService = userService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        List<UserResponse> users =
                userService.getAllUsers();

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        users,
                        "Users fetched successfully"
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @RequestBody CreateUserRequest request
    ) {

        UserResponse response =
                userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        apiResponseFactory.success(
                                response,
                                "User created successfully"
                        )
                );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request
    ) {

        UserResponse response =
                userService.updateUser(id, request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "User updated successfully"
                )
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<UserResponse>> toggleUserStatus(
            @PathVariable UUID id
    ) {

        UserResponse response =
                userService.toggleUserStatus(id);

        String message = response.getActive()
                ? "User activated successfully"
                : "User deactivated successfully";

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        message
                )
        );
    }
}