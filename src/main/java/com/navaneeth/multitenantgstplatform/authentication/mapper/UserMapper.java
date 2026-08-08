package com.navaneeth.multitenantgstplatform.authentication.mapper;

import com.navaneeth.multitenantgstplatform.authentication.dto.CreateUserRequest;
import com.navaneeth.multitenantgstplatform.authentication.dto.UpdateUserRequest;
import com.navaneeth.multitenantgstplatform.authentication.dto.UserResponse;
import com.navaneeth.multitenantgstplatform.authentication.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "tenant", ignore = true)
    User toEntity(CreateUserRequest request);

    @Mapping(source = "tenant.id", target = "tenantId")
    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UpdateUserRequest request, @MappingTarget User user);
}
