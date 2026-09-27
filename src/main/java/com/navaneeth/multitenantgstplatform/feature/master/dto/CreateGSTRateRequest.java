package com.navaneeth.multitenantgstplatform.feature.master.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateGSTRateRequest(

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        @DecimalMax(value = "100.0", inclusive = true)
        @Digits(integer = 3, fraction = 2)
        BigDecimal ratePercent

) {
}