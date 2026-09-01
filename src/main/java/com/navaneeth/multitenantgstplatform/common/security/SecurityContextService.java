package com.navaneeth.multitenantgstplatform.common.security;

import com.navaneeth.multitenantgstplatform.authentication.dto.JwtTokenData;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityContextService {

    public JwtTokenData getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new IllegalStateException("User is not authenticated");
        }

        return (JwtTokenData) authentication.getPrincipal();
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().userId();
    }

    public UUID getCurrentTenantId() {
        return getCurrentUser().tenantId();
    }
}