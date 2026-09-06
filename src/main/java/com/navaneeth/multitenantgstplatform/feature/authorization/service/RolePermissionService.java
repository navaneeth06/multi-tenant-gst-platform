package com.navaneeth.multitenantgstplatform.feature.authorization.service;

import com.navaneeth.multitenantgstplatform.feature.authorization.dto.RolePermissionRequest;
import com.navaneeth.multitenantgstplatform.feature.authorization.dto.RolePermissionResponse;

import java.util.UUID;

public interface RolePermissionService {

    RolePermissionResponse getPermissions(UUID roleId);

    RolePermissionResponse updatePermissions(
            UUID roleId,
            RolePermissionRequest request
    );
}