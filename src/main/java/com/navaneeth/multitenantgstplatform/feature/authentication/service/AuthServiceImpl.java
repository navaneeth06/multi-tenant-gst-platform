package com.navaneeth.multitenantgstplatform.feature.authentication.service;

import com.navaneeth.multitenantgstplatform.common.exception.InvalidCredentialsException;
import com.navaneeth.multitenantgstplatform.common.exception.UserInactiveException;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.JwtTokenData;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.LoginRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.LoginResponse;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UserResponse;
import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;
import com.navaneeth.multitenantgstplatform.feature.authentication.repository.UserRepository;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.TenantUser;
import com.navaneeth.multitenantgstplatform.feature.authorization.repository.TenantUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TenantUserRepository tenantUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            TenantUserRepository tenantUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.tenantUserRepository = tenantUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(
            LoginRequest request
    ) {

        /*
         * Find the global user.
         */
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid username or password"
                        )
                );

        /*
         * Check whether the user is active.
         */
        if (!user.getActive()) {
            throw new UserInactiveException(
                    "User account is inactive"
            );
        }

        /*
         * Verify password.
         */
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid username or password"
            );
        }

        /*
         * Get all tenant memberships for this user.
         *
         * Example:
         *
         * Tenant A → Admin
         * Tenant B → Accountant
         * Tenant C → Staff
         */
        List<TenantUser> tenantUsers =
                tenantUserRepository.findByUserId(
                        user.getId()
                );

        /*
         * Convert tenant memberships into
         * JWT tenant → role mappings.
         */
        List<JwtTokenData.TenantRoleData> memberships =
                tenantUsers.stream()
                        .map(tenantUser ->
                                new JwtTokenData.TenantRoleData(
                                        tenantUser.getTenant().getId(),
                                        tenantUser.getRole().getId()
                                )
                        )
                        .toList();

        /*
         * Generate JWT containing all
         * tenant → role mappings.
         */
        String accessToken =
                jwtService.generateAccessToken(
                        user,
                        memberships
                );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(
                        jwtService.getAccessTokenExpiration() / 1000
                )
                .user(
                        UserResponse.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .active(user.getActive())
                                .build()
                )
                .build();
    }
}
