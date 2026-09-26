package com.navaneeth.multitenantgstplatform.feature.branch.dto;

import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateBranchRequest(

        @Size(max = 150)
        String name,

        @Size(max = 2000)
        String address,

        UUID stateId,

        @Size(max = 10)
        String pincode

) {
}