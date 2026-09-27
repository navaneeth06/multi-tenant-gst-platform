package com.navaneeth.multitenantgstplatform.feature.authentication.repository;

import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByIdAndTenantId(UUID id, UUID tenantId);

    @Query("""
            SELECT DISTINCT u
            FROM User u
            LEFT JOIN FETCH u.roles
            LEFT JOIN FETCH u.branchAccess ba
            LEFT JOIN FETCH ba.branch
            WHERE u.tenant.id = :tenantId
            """)
    List<User> findAllByTenantIdWithDetails(UUID tenantId);
}