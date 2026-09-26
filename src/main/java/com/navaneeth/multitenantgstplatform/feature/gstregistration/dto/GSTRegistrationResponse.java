package com.navaneeth.multitenantgstplatform.feature.gstregistration.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record GSTRegistrationResponse(

        UUID id,
        UUID tenantId,
        UUID stateId,
        String stateName,
        String gstin,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt

) {
}