package com.navaneeth.multitenantgstplatform.authentication.dto;

import java.util.UUID;

public record JwtTokenData(
        UUID userId,
        UUID tenantId,
        String username
) {
}