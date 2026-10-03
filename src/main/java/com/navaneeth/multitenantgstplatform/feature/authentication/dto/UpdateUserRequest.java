package com.navaneeth.multitenantgstplatform.feature.authentication.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    private String name;
    private String username;
    private String email;

    private List<UUID> roleIds;
    private List<UUID> branchIds;
}