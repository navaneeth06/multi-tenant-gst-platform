package com.navaneeth.multitenantgstplatform.feature.authentication.security;

import java.util.List;
import java.util.UUID;

public record JwtAuthentication(
        UUID userId,
        String username,
        List<Membership> memberships,
        UUID currentTenantId,
        UUID currentRoleId
) {

    public record Membership(
            UUID tenantId,
            UUID roleId
    ) {
    }

}