package com.navaneeth.multitenantgstplatform.feature.gstregistration.mapper;

import com.navaneeth.multitenantgstplatform.feature.gstregistration.dto.CreateGSTRegistrationRequest;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.dto.GSTRegistrationResponse;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.entity.GSTRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GSTRegistrationMapper {

    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "status", ignore = true)
    GSTRegistration toEntity(CreateGSTRegistrationRequest request);

    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.name", target = "stateName")
    GSTRegistrationResponse toResponse(GSTRegistration registration);
}