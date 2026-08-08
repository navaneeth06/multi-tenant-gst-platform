package com.navaneeth.multitenantgstplatform.authentication.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateUserRequest {
    private UUID tenantId;
    private String name;
    private String email;
    private String username;
    private String password;
}
