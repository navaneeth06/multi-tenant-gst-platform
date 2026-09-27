package com.navaneeth.multitenantgstplatform.feature.master.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record HsnCodeResponse(

        UUID id,
        String code,
        String description,
        UUID gstRateId,
        BigDecimal gstRatePercent

) {
}