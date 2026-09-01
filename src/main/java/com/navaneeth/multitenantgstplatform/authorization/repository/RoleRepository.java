package com.navaneeth.multitenantgstplatform.authorization.repository;

import com.navaneeth.multitenantgstplatform.authorization.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    List<Role> findByTenantId(UUID id);

    Optional<Role> findByIdAndTenantId(UUID id, UUID tenantId);

    boolean existsByTenantIdAndSlug(UUID tenantId, String slug);
}
