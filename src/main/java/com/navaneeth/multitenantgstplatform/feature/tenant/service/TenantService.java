package com.navaneeth.multitenantgstplatform.feature.tenant.service;

import com.navaneeth.multitenantgstplatform.feature.tenant.dto.CreateTenantRequest;
import com.navaneeth.multitenantgstplatform.feature.tenant.dto.TenantResponse;
import com.navaneeth.multitenantgstplatform.feature.tenant.dto.UpdateTenantRequest;

import java.util.List;
import java.util.UUID;

public interface TenantService {

    List<TenantResponse> getAllTenants();

    TenantResponse createTenant(CreateTenantRequest request);

    TenantResponse updateTenant(UUID id, UpdateTenantRequest request);

    TenantResponse toggleTenantStatus(UUID id);
}