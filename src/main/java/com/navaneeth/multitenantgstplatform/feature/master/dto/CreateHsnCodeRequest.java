package com.navaneeth.multitenantgstplatform.feature.master.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateHsnCodeRequest(

        @NotBlank
        @Size(max = 20)
        String code,

        @Size(max = 300)
        String description,

        UUID gstRateId

) {
}