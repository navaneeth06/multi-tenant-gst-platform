package com.navaneeth.multitenantgstplatform.tenant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTenantRequest {

    private String name;

    private String slug;

    private String gstin;

    private String email;

    private String phone;

    private Boolean active;

}