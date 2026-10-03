package com.navaneeth.multitenantgstplatform.feature.customer.repository;

import com.navaneeth.multitenantgstplatform.feature.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    List<Customer> findByTenantId(UUID tenantId);

    Optional<Customer> findByIdAndTenantId(UUID id, UUID tenantId);

    @Query("""
            SELECT c FROM Customer c
            WHERE c.tenant.id = :tenantId
              AND (
                   LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%'))
                OR c.phone LIKE CONCAT('%', :search, '%')
              )
            """)
    List<Customer> searchByTenantId(
            @Param("tenantId") UUID tenantId,
            @Param("search") String search
    );
}