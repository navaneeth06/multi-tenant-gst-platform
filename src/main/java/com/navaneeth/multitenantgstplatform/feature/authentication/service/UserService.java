package com.navaneeth.multitenantgstplatform.feature.authentication.service;

import com.navaneeth.multitenantgstplatform.feature.authentication.dto.CreateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UpdateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(UUID id, UpdateUserRequest request);

    UserResponse toggleUserStatus(UUID id);

    void grantBranchAccess(UUID userId, UUID branchId);

    void revokeBranchAccess(UUID userId, UUID branchId);
}
