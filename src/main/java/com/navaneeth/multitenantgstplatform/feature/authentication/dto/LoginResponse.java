package com.navaneeth.multitenantgstplatform.feature.authentication.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoginResponse {
    private String accessToken;

    private String tokenType;

    private long expiresIn;

    private UserResponse user;

}
