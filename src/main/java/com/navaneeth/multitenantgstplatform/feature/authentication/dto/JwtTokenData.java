package com.navaneeth.multitenantgstplatform.feature.authentication.dto;

import java.util.List;
import java.util.UUID;

public record JwtTokenData(
        UUID userId,
        String username,
        List<TenantRoleData> memberships
) {

    public record TenantRoleData(
            UUID tenantId,
            UUID roleId
    ) {
    }
}