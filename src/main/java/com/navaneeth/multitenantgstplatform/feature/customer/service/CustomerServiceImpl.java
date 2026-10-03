package com.navaneeth.multitenantgstplatform.feature.customer.service;

import com.navaneeth.multitenantgstplatform.feature.customer.dto.CreateCustomerRequest;
import com.navaneeth.multitenantgstplatform.feature.customer.dto.CustomerResponse;
import com.navaneeth.multitenantgstplatform.feature.customer.dto.UpdateCustomerRequest;
import com.navaneeth.multitenantgstplatform.feature.customer.entity.Customer;
import com.navaneeth.multitenantgstplatform.feature.customer.mapper.CustomerMapper;
import com.navaneeth.multitenantgstplatform.feature.customer.repository.CustomerRepository;
import com.navaneeth.multitenantgstplatform.feature.master.entity.State;
import com.navaneeth.multitenantgstplatform.feature.master.repository.StateRepository;
import com.navaneeth.multitenantgstplatform.feature.tenant.entity.Tenant;
import com.navaneeth.multitenantgstplatform.feature.tenant.repository.TenantRepository;
import com.navaneeth.multitenantgstplatform.infrastructure.security.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final TenantRepository tenantRepository;
    private final StateRepository stateRepository;
    private final CustomerMapper customerMapper;
    private final SecurityContextService securityContextService;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {

        UUID tenantId = getCurrentTenantId();

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        State state = stateRepository.findById(request.stateId())
                .orElseThrow(() -> new IllegalArgumentException("State not found"));

        Customer customer = customerMapper.toEntity(request);
        customer.setTenant(tenant);
        customer.setState(state);

        Customer saved = customerRepository.save(customer);

        return customerMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomers(String search) {

        UUID tenantId = getCurrentTenantId();

        List<Customer> customers = StringUtils.hasText(search)
                ? customerRepository.searchByTenantId(tenantId, search)
                : customerRepository.findByTenantId(tenantId);

        return customers.stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(UUID id) {

        UUID tenantId = getCurrentTenantId();

        Customer customer = customerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest request) {

        UUID tenantId = getCurrentTenantId();

        Customer customer = customerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        customerMapper.updateCustomer(request, customer);

        if (request.stateId() != null) {
            State state = stateRepository.findById(request.stateId())
                    .orElseThrow(() -> new IllegalArgumentException("State not found"));
            customer.setState(state);
        }

        Customer updated = customerRepository.save(customer);

        return customerMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID id) {

        UUID tenantId = getCurrentTenantId();

        Customer customer = customerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        try {
            customerRepository.delete(customer);
            customerRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "Cannot delete this customer — they have existing invoices on record"
            );
        }
    }

    private UUID getCurrentTenantId() {
        UUID tenantId = securityContextService.getCurrentTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("No authenticated tenant context");
        }
        return tenantId;
    }
}