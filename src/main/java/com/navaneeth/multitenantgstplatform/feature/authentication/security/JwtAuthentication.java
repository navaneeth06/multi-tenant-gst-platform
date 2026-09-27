package com.navaneeth.multitenantgstplatform.feature.authentication.security;

import java.util.List;
import java.util.UUID;

public record JwtAuthentication(
        UUID userId,
        String username,
        UUID tenantId,
        List<UUID> roleIds
) {
}