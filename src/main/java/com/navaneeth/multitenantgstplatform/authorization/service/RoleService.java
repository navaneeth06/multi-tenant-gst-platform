package com.navaneeth.multitenantgstplatform.authorization.service;

import com.navaneeth.multitenantgstplatform.authorization.dto.CreateRoleRequest;
import com.navaneeth.multitenantgstplatform.authorization.dto.RoleResponse;
import com.navaneeth.multitenantgstplatform.authorization.dto.UpdateRoleRequest;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<RoleResponse> getRoles();

    RoleResponse createRole(CreateRoleRequest request);

    RoleResponse updateRole(UUID id, UpdateRoleRequest request);
}
