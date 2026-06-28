package com.navaneeth.multitenantgstplatform.tenant.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class TenantResponse {

    private UUID id;

    private String name;

}