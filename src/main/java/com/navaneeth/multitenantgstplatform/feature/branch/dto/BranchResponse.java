package com.navaneeth.multitenantgstplatform.feature.branch.dto;

import com.navaneeth.multitenantgstplatform.feature.branch.entity.Branch.BranchStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BranchResponse(

        UUID id,
        UUID tenantId,
        String name,
        String address,
        UUID stateId,
        String stateName,
        String pincode,
        BranchStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt

) {
}