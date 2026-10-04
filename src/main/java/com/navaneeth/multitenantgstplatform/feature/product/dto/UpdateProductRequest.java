package com.navaneeth.multitenantgstplatform.feature.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductRequest(

        @Size(max = 200)
        String name,

        @Size(max = 100)
        String sku,

        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal price,

        UUID hsnCodeId,

        UUID gstRateId,

        UUID unitId,

        Boolean active

) {
}