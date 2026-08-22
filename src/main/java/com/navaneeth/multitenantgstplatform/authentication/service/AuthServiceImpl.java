package com.navaneeth.multitenantgstplatform.authentication.service;

import com.navaneeth.multitenantgstplatform.authentication.dto.LoginRequest;
import com.navaneeth.multitenantgstplatform.authentication.dto.LoginResponse;
import com.navaneeth.multitenantgstplatform.authentication.dto.UserResponse;
import com.navaneeth.multitenantgstplatform.authentication.entity.User;
import com.navaneeth.multitenantgstplatform.authentication.repository.UserRepository;
import com.navaneeth.multitenantgstplatform.common.exception.InvalidCredentialsException;
import com.navaneeth.multitenantgstplatform.common.exception.UserInactiveException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(
            UUID tenantId,
            LoginRequest request
    ) {

        User user = userRepository
                .findByTenant_IdAndUsername(
                        tenantId,
                        request.getUsername()
                )
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid username or password"
                        )
                );

        if (!user.getActive()) {
            throw new UserInactiveException("User account is inactive");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid username or password"
            );
        }

        // JWT generation will be added next.
        String accessToken = jwtService.generateAccessToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpiration() / 1000)
                .user(
                        UserResponse.builder()
                                .id(user.getId())
                                .tenantId(user.getTenant().getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .active(user.getActive())
                                .build()
                )
                .build();
    }
}
