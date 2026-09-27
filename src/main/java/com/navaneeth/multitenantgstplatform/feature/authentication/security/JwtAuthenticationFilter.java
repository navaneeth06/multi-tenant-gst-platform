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

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private UsernamePasswordAuthenticationToken createAuthentication(JwtTokenData tokenData) {

        JwtAuthentication principal = new JwtAuthentication(
                tokenData.userId(),
                tokenData.username(),
                tokenData.tenantId(),
                tokenData.roleIds()
        );

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                Collections.emptyList()
        );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/v1/auth/login");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // No JWT → continue as anonymous
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            JwtTokenData tokenData = jwtService.extractTokenData(token);

            UsernamePasswordAuthenticationToken authentication = createAuthentication(tokenData);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (InvalidTokenException exception) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}