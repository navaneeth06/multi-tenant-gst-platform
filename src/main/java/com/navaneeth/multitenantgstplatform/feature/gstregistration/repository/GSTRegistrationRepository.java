package com.navaneeth.multitenantgstplatform.feature.gstregistration.repository;

import com.navaneeth.multitenantgstplatform.feature.gstregistration.entity.GSTRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GSTRegistrationRepository extends JpaRepository<GSTRegistration, UUID> {

    List<GSTRegistration> findByTenantId(UUID tenantId);

    Optional<GSTRegistration> findByIdAndTenantId(
            UUID id,
            UUID tenantId
    );

    Optional<GSTRegistration> findByTenantIdAndStateId(
            UUID tenantId,
            UUID stateId
    );

    boolean existsByTenantIdAndGstin(
            UUID tenantId,
            String gstin
    );
}