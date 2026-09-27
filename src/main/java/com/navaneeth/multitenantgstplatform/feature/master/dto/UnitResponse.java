package com.navaneeth.multitenantgstplatform.feature.master.dto;

import java.util.UUID;

public record UnitResponse(

        UUID id,
        String name,
        String abbreviation

) {
}