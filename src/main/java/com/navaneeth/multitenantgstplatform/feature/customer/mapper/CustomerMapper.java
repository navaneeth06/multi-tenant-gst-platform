package com.navaneeth.multitenantgstplatform.feature.customer.mapper;

import com.navaneeth.multitenantgstplatform.feature.customer.dto.CreateCustomerRequest;
import com.navaneeth.multitenantgstplatform.feature.customer.dto.CustomerResponse;
import com.navaneeth.multitenantgstplatform.feature.customer.dto.UpdateCustomerRequest;
import com.navaneeth.multitenantgstplatform.feature.customer.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "state", ignore = true)
    Customer toEntity(CreateCustomerRequest request);

    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.name", target = "stateName")
    CustomerResponse toResponse(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "state", ignore = true)
    void updateCustomer(
            UpdateCustomerRequest request,
            @MappingTarget Customer customer
    );
}