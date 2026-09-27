package com.navaneeth.multitenantgstplatform.feature.master.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record GSTRateResponse(

        UUID id,
        BigDecimal ratePercent,
        boolean active

) {
}