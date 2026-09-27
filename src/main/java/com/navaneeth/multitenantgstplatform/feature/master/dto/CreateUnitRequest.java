package com.navaneeth.multitenantgstplatform.feature.master.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUnitRequest(

        @NotBlank
        @Size(max = 50)
        String name,

        @Size(max = 10)
        String abbreviation

) {
}