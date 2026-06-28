package com.navaneeth.multitenantgstplatform.tenant.service;

import com.navaneeth.multitenantgstplatform.tenant.dto.CreateTenantRequest;
import com.navaneeth.multitenantgstplatform.tenant.dto.TenantResponse;

import java.util.List;

public interface TenantService {

    List<TenantResponse> getAllTenants();

    TenantResponse createTenant(CreateTenantRequest request);

}