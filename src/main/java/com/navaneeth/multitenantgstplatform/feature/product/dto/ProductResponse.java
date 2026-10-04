package com.navaneeth.multitenantgstplatform.feature.product.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ProductResponse(

        UUID id,
        UUID tenantId,
        String name,
        String sku,
        BigDecimal price,
        UUID hsnCodeId,
        String hsnCode,
        UUID gstRateId,
        BigDecimal gstRatePercent,
        UUID unitId,
        String unitName,
        boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt

) {
}