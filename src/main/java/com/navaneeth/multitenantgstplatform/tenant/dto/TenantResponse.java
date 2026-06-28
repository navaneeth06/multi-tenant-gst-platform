package com.navaneeth.multitenantgstplatform.tenant.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class TenantResponse {

    private UUID id;

    private String name;

    private String slug;

    private String gstin;

    private String email;

    private String phone;

    private boolean active;
}