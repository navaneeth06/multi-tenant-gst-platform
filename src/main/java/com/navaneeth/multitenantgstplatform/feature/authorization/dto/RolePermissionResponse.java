package com.navaneeth.multitenantgstplatform.feature.authorization.dto;

import java.util.Set;
import java.util.UUID;

public record RolePermissionResponse(

        UUID roleId,
        String roleName,
        Set<PermissionItem> permissions

) {

    public record PermissionItem(

            UUID id,
            UUID moduleId,
            String moduleName,
            String action

    ) {
    }
}