package com.navaneeth.multitenantgstplatform.feature.authentication.mapper;

import com.navaneeth.multitenantgstplatform.feature.authentication.dto.CreateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UpdateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UserResponse;
import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.TenantUser;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(CreateUserRequest request);

    @Mapping(target = "memberships", source = "memberships")
    UserResponse toResponse(User user);

    List<UserResponse.MembershipResponse> toMembershipResponses(
            List<TenantUser> memberships
    );

    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "tenant.name", target = "tenantName")
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "role.slug", target = "roleSlug")
    UserResponse.MembershipResponse toMembershipResponse(
            TenantUser tenantUser
    );

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateUser(
            UpdateUserRequest request,
            @MappingTarget User user
    );
}