package com.navaneeth.multitenantgstplatform.authorization.dto;

import java.util.UUID;

public record UpdateRoleRequest(
        UUID id,
        String name,
        String description
) {
}


