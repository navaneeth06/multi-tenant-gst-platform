package com.navaneeth.multitenantgstplatform.authentication.repository;

import com.navaneeth.multitenantgstplatform.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByTenant_IdAndUsername(UUID tenantId, String username);
}
