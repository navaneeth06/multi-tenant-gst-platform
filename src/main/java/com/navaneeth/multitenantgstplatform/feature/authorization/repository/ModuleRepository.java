package com.navaneeth.multitenantgstplatform.feature.authorization.repository;

import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Module, UUID> {

    boolean existsBySlug(String slug);
}