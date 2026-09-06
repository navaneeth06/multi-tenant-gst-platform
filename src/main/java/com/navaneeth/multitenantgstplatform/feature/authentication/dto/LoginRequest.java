package com.navaneeth.multitenantgstplatform.feature.authentication.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoginRequest {
    private String username;

    private String password;
}
