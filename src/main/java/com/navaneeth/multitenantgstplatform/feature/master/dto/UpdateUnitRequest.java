package com.navaneeth.multitenantgstplatform.feature.master.dto;

import jakarta.validation.constraints.Size;

public record UpdateUnitRequest(

        @Size(max = 50)
        String name,

        @Size(max = 10)
        String abbreviation

) {
}