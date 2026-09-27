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

    // Roles this user gets — always resolved and validated within the
    // CALLER's own tenant server-side, never a client-supplied tenantId.
    private List<UUID> roleIds;

    // Branches (within that same tenant) this user is granted access to.
    private List<UUID> branchIds;
}