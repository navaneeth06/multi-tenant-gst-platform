package com.navaneeth.multitenantgstplatform.feature.master.repository;

import com.navaneeth.multitenantgstplatform.feature.master.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);
}