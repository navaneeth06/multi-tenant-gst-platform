package com.navaneeth.multitenantgstplatform.feature.master.dto;

import java.util.UUID;

public record StateResponse(
        UUID id,
        String name,
        String code,
        String gstCode
) {
}