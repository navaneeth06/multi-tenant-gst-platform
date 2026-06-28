package com.navaneeth.multitenantgstplatform.tenant.service;

import com.navaneeth.multitenantgstplatform.tenant.dto.CreateTenantRequest;
import com.navaneeth.multitenantgstplatform.tenant.dto.TenantResponse;
import com.navaneeth.multitenantgstplatform.tenant.dto.UpdateTenantRequest;
import com.navaneeth.multitenantgstplatform.tenant.entity.Tenant;
import com.navaneeth.multitenantgstplatform.tenant.mapper.TenantMapper;
import com.navaneeth.multitenantgstplatform.tenant.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    public TenantServiceImpl(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    @Override
    public List<TenantResponse> getAllTenants() {

        List<Tenant> tenants = tenantRepository.findAll();

        return tenants.stream()
                .map(tenant -> TenantResponse.builder()
                        .id(tenant.getId())
                        .name(tenant.getName())
                        .slug(tenant.getSlug())
                        .gstin(tenant.getGstin())
                        .email(tenant.getEmail())
                        .phone(tenant.getPhone())
                        .active(tenant.getActive())
                        .build())
                .toList();
    }

    @Override
    public TenantResponse createTenant(CreateTenantRequest request) {

        Tenant tenant = Tenant.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .email(request.getEmail())
                .phone(request.getPhone())
                .active(true)
                .build();

        Tenant savedTenant = tenantRepository.save(tenant);

        return TenantResponse.builder()
                .id(savedTenant.getId())
                .name(savedTenant.getName())
                .build();
    }

    @Override
    public TenantResponse updateTenant(UUID id, UpdateTenantRequest request) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        tenantMapper.updateTenant(request, tenant);

        tenant.setUpdatedAt(LocalDateTime.now());

        tenantRepository.save(tenant);

        return tenantMapper.toResponse(tenant);
    }
}
