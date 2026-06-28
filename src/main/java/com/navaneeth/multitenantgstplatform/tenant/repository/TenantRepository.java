package com.navaneeth.multitenantgstplatform.tenant.repository;

import com.navaneeth.multitenantgstplatform.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

}