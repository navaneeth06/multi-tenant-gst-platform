package com.navaneeth.multitenantgstplatform.infrastructure.security;

import com.navaneeth.multitenantgstplatform.feature.authentication.security.JwtAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityContextService {

    public JwtAuthentication getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new IllegalStateException(
                    "User is not authenticated"
            );
        }

        return (JwtAuthentication) authentication.getPrincipal();
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().userId();
    }

    public UUID getCurrentTenantId() {
        return getCurrentUser().tenantId();
    }

    public UUID getCurrentRoleId() {
        return getCurrentUser().roleId();
    }
}