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

    private UUID tenantId;
    private String tenantName;

    private List<RoleResponse> roles;
    private List<BranchAccessResponse> branchAccess;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoleResponse {
        private UUID id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BranchAccessResponse {
        private UUID branchId;
        private String branchName;
    }
}