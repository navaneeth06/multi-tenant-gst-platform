package com.navaneeth.multitenantgstplatform.feature.authorization.repository;

import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Module;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    boolean existsByModuleAndAction(
            Module module,
            String action
    );
}