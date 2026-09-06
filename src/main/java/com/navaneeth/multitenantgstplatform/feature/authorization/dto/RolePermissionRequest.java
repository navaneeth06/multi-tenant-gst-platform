package com.navaneeth.multitenantgstplatform.feature.authorization.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record RolePermissionRequest(
        @NotNull
        Set<UUID> permissionIds
) {
}
