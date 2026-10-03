package com.navaneeth.multitenantgstplatform.feature.customer.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CustomerResponse(

        UUID id,
        UUID tenantId,
        String name,
        String gstin,
        String phone,
        String email,
        String address,
        UUID stateId,
        String stateName,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt

) {
}