package com.navaneeth.multitenantgstplatform.feature.gstregistration.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CreateGSTRegistrationRequest(

        @NotNull
        UUID stateId,

        @NotNull
        @Pattern(
                regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
                message = "GSTIN format is invalid"
        )
        String gstin

) {
}