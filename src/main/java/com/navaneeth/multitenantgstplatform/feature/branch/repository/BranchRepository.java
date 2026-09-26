package com.navaneeth.multitenantgstplatform.feature.branch.repository;

import com.navaneeth.multitenantgstplatform.feature.branch.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {

    List<Branch> findByTenantId(UUID tenantId);

    Optional<Branch> findByIdAndTenantId(
            UUID id,
            UUID tenantId
    );

    boolean existsByTenantIdAndName(
            UUID tenantId,
            String name
    );
}