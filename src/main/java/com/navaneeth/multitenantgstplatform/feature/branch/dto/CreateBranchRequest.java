package com.navaneeth.multitenantgstplatform.feature.branch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateBranchRequest(

        @NotBlank
        @Size(max = 150)
        String name,

        @Size(max = 2000)
        String address,

        @NotNull
        UUID gstRegistrationId,

        @Size(max = 10)
        String pincode

) {
}