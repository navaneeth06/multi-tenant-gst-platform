package com.navaneeth.multitenantgstplatform.feature.authorization.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RoleResponse(
        UUID id,
        UUID tenantId,
        String name,
        String slug,
        String description
) {
}
