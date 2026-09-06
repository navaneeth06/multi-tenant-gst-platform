package com.navaneeth.multitenantgstplatform.feature.authentication.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    private String name;

    private String email;

    private String username;

    private String password;

    private List<TenantRoleRequest> memberships;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TenantRoleRequest {

        private UUID tenantId;

        private UUID roleId;
    }
}