package com.navaneeth.multitenantgstplatform.feature.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateTenantRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String slug;

    private String gstin;

    @NotBlank
    private String email;

    private String phone;

}