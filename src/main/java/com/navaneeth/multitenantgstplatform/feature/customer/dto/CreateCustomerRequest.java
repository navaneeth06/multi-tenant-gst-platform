package com.navaneeth.multitenantgstplatform.feature.customer.dto;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record CreateCustomerRequest(

        @NotBlank
        @Size(max = 200)
        String name,

        @Pattern(
                regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
                message = "GSTIN format is invalid"
        )
        String gstin,

        @Size(max = 20)
        String phone,

        @Email
        @Size(max = 200)
        String email,

        @Size(max = 2000)
        String address,

        @NotNull
        UUID stateId

) {
}