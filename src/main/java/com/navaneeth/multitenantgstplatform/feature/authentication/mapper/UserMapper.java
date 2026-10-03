package com.navaneeth.multitenantgstplatform.feature.authentication.mapper;

import com.navaneeth.multitenantgstplatform.feature.authentication.dto.CreateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UpdateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UserResponse;
import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Role;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.UserBranchAccess;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "branchAccess", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(CreateUserRequest request);

    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "tenant.name", target = "tenantName")
    @Mapping(source = "roles", target = "roles")
    @Mapping(source = "branchAccess", target = "branchAccess")
    UserResponse toResponse(User user);

    UserResponse.RoleResponse toRoleResponse(Role role);

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    UserResponse.BranchAccessResponse toBranchAccessResponse(UserBranchAccess access);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "branchAccess", ignore = true)
    void updateUser(UpdateUserRequest request, @MappingTarget User user);
}