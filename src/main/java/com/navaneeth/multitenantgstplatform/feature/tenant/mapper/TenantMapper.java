package com.navaneeth.multitenantgstplatform.feature.tenant.mapper;

import com.navaneeth.multitenantgstplatform.feature.tenant.dto.CreateTenantRequest;
import com.navaneeth.multitenantgstplatform.feature.tenant.dto.TenantResponse;
import com.navaneeth.multitenantgstplatform.feature.tenant.dto.UpdateTenantRequest;
import com.navaneeth.multitenantgstplatform.feature.tenant.entity.Tenant;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    Tenant toEntity(CreateTenantRequest request);

    TenantResponse toResponse(Tenant tenant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTenant(UpdateTenantRequest request,
                      @MappingTarget Tenant tenant);

}