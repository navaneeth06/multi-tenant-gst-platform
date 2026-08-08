package com.navaneeth.multitenantgstplatform.authentication.service;

import com.navaneeth.multitenantgstplatform.authentication.dto.CreateUserRequest;
import com.navaneeth.multitenantgstplatform.authentication.dto.UpdateUserRequest;
import com.navaneeth.multitenantgstplatform.authentication.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(UUID id, UpdateUserRequest request);

    UserResponse toggleUserStatus(UUID id);
}
