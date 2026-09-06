package com.navaneeth.multitenantgstplatform.feature.authentication.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private String username;
    private Boolean active;

    private List<MembershipResponse> memberships;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MembershipResponse {

        private UUID tenantId;
        private String tenantName;

        private UUID roleId;
        private String roleName;
        private String roleSlug;
    }
}