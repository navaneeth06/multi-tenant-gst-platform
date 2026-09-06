package com.navaneeth.multitenantgstplatform.feature.authorization.mapper;

import com.navaneeth.multitenantgstplatform.feature.authorization.dto.CreateRoleRequest;
import com.navaneeth.multitenantgstplatform.feature.authorization.dto.RoleResponse;
import com.navaneeth.multitenantgstplatform.feature.authorization.dto.UpdateRoleRequest;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Role;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "tenant", ignore = true)
    Role toEntity(CreateRoleRequest request);

    @Mapping(source = "tenant.id", target = "tenantId")
    RoleResponse toResponse(Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRole(UpdateRoleRequest request, @MappingTarget Role role);

}
