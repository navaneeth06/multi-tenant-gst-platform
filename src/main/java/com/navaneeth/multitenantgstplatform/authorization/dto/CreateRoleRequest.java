package com.navaneeth.multitenantgstplatform.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRoleRequest(

        @NotBlank(message = "Role name is required")
        @Size(max = 100, message = "Role name must not exceed 100 characters")
        String name,
        @NotBlank(message = "Role slug is required")
        String slug,
        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description
) {
}