package com.navaneeth.multitenantgstplatform.feature.authorization.dto;

import java.util.UUID;

public record UpdateRoleRequest(
        UUID id,
        String name,
        String description
) {
}


