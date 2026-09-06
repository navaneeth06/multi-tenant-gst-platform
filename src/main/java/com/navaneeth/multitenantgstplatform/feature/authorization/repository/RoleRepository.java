package com.navaneeth.multitenantgstplatform.feature.authorization.repository;

import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByTenantIdIsNullAndSlug(String slug);

    Optional<Role> findByIdAndTenantId(UUID roleId, UUID tenantId);

    List<Role> findByTenantId(UUID tenantId);

    boolean existsByTenantIdAndSlug(
            UUID tenantId,
            String slug
    );
}