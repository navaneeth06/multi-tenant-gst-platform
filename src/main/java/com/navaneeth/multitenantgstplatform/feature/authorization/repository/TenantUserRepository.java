package com.navaneeth.multitenantgstplatform.feature.authorization.repository;

import com.navaneeth.multitenantgstplatform.feature.authorization.entity.TenantUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantUserRepository
        extends JpaRepository<TenantUser, UUID> {

    Optional<TenantUser> findByTenantIdAndUserId(
            UUID tenantId,
            UUID userId
    );

    List<TenantUser> findByTenantId(UUID tenantId);

    List<TenantUser> findByUserId(UUID userId);

    boolean existsByTenantIdAndUserId(
            UUID tenantId,
            UUID userId
    );

    void deleteByTenantIdAndUserId(
            UUID tenantId,
            UUID userId
    );
}