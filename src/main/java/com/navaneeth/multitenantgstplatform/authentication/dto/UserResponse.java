package com.navaneeth.multitenantgstplatform.authentication.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private UUID id;

    private UUID tenantId;

    private String name;

    private String email;

    private String username;

    private Boolean active;
}
