package com.navaneeth.multitenantgstplatform.feature.customer.service;

import com.navaneeth.multitenantgstplatform.feature.customer.dto.CreateCustomerRequest;
import com.navaneeth.multitenantgstplatform.feature.customer.dto.CustomerResponse;
import com.navaneeth.multitenantgstplatform.feature.customer.dto.UpdateCustomerRequest;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    CustomerResponse createCustomer(CreateCustomerRequest request);

    List<CustomerResponse> getCustomers(String search);

    CustomerResponse getCustomer(UUID id);

    CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest request);

    void deleteCustomer(UUID id);
}