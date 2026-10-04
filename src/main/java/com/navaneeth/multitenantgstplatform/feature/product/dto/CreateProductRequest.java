package com.navaneeth.multitenantgstplatform.feature.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequest(

        @NotBlank
        @Size(max = 200)
        String name,

        @NotBlank
        @Size(max = 100)
        String sku,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal price,

        @NotNull
        UUID hsnCodeId,

        @NotNull
        UUID gstRateId,

        @NotNull
        UUID unitId

) {
}