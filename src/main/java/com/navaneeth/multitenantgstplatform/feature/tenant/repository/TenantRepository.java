package com.navaneeth.multitenantgstplatform.feature.tenant.repository;

import com.navaneeth.multitenantgstplatform.feature.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {

}