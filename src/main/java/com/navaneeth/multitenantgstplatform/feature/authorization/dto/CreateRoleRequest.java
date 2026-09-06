package com.navaneeth.multitenantgstplatform.feature.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateRoleRequest(

        @NotBlank(message = "Role name is required")
        @Size(
                max = 100,
                message = "Role name must not exceed 100 characters"
        )
        String name,

        @NotBlank(message = "Role slug is required")
        @Size(
                max = 100,
                message = "Role slug must not exceed 100 characters"
        )
        String slug,

        @Size(
                max = 500,
                message = "Description must not exceed 500 characters"
        )
        String description,

        UUID tenantId
) {
}