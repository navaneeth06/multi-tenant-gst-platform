package com.navaneeth.multitenantgstplatform.feature.authentication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPoint
        implements org.springframework.security.web.AuthenticationEntryPoint {

    private final ApiResponseFactory apiResponseFactory;
    private final ObjectMapper objectMapper;

    public AuthenticationEntryPoint(
            ApiResponseFactory apiResponseFactory,
            ObjectMapper objectMapper
    ) {
        this.apiResponseFactory = apiResponseFactory;
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String authorizationHeader =
                request.getHeader("Authorization");

        String message;

        if (authorizationHeader != null &&
                authorizationHeader.startsWith("Bearer ")) {

            message = "Invalid or expired token";
            System.out.println(exception.getMessage());

        } else {

            message = "Authentication required";
        }

        ApiResponse<Void> apiResponse =
                apiResponseFactory.error(message);

        objectMapper.writeValue(
                response.getOutputStream(),
                apiResponse
        );
    }
}