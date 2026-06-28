package com.navaneeth.multitenantgstplatform.tenant.repository;

import com.navaneeth.multitenantgstplatform.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {

}