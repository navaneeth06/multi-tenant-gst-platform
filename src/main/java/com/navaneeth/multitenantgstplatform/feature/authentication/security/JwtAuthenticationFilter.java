package com.navaneeth.multitenantgstplatform.feature.authentication.security;

import com.navaneeth.multitenantgstplatform.common.exception.InvalidTokenException;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.JwtTokenData;
import com.navaneeth.multitenantgstplatform.feature.authentication.service.JwtService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TENANT_ID_HEADER = "X-Tenant-Id";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private UsernamePasswordAuthenticationToken createAuthentication(
            JwtTokenData tokenData,
            UUID tenantId,
            UUID roleId
    ) {

        JwtAuthentication principal =
                new JwtAuthentication(
                        tokenData.userId(),
                        tokenData.username(),
                        tenantId,
                        roleId
                );

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                Collections.emptyList()
        );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        return request.getServletPath()
                .equals("/api/v1/auth/login");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        // No JWT → continue as anonymous
        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authorizationHeader.substring(7);

        try {

            // -----------------------------------------
            // 1. Validate JWT
            // -----------------------------------------

            JwtTokenData tokenData =
                    jwtService.extractTokenData(token);


            // -----------------------------------------
            // 2. Check X-Tenant-Id
            // -----------------------------------------

            String tenantIdHeader =
                    request.getHeader(TENANT_ID_HEADER);


            // -----------------------------------------
            // 3. No tenant selected
            // -----------------------------------------

            if (tenantIdHeader == null ||
                    tenantIdHeader.isBlank()) {

                /*
                 * User is authenticated,
                 * but there is no tenant context.
                 *
                 * Example:
                 *
                 * POST /api/v1/tenants
                 * Authorization: Bearer <token>
                 *
                 * or a future global endpoint.
                 */

                UsernamePasswordAuthenticationToken authentication =
                        createAuthentication(
                                tokenData,
                                null,
                                null
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                filterChain.doFilter(request, response);
                return;
            }


            // -----------------------------------------
            // 4. Parse tenant ID
            // -----------------------------------------

            UUID requestedTenantId;

            try {

                requestedTenantId =
                        UUID.fromString(tenantIdHeader);

            } catch (IllegalArgumentException exception) {

                SecurityContextHolder.clearContext();

                filterChain.doFilter(request, response);
                return;
            }


            // -----------------------------------------
            // 5. Find tenant membership
            // -----------------------------------------

            JwtTokenData.TenantRoleData membership =
                    tokenData.memberships()
                            .stream()
                            .filter(item ->
                                    item.tenantId()
                                            .equals(requestedTenantId)
                            )
                            .findFirst()
                            .orElse(null);


            // -----------------------------------------
            // 6. User doesn't belong to tenant
            // -----------------------------------------

            if (membership == null) {

                SecurityContextHolder.clearContext();

                filterChain.doFilter(request, response);
                return;
            }


            // -----------------------------------------
            // 7. Authenticate with tenant context
            // -----------------------------------------

            UsernamePasswordAuthenticationToken authentication =
                    createAuthentication(
                            tokenData,
                            membership.tenantId(),
                            membership.roleId()
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (InvalidTokenException exception) {

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}